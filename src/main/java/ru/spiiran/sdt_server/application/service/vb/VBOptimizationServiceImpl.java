package ru.spiiran.sdt_server.application.service.vb;

import org.springframework.stereotype.Service;
import ru.spiiran.sdt_server.api.v1.dto.vb.DtoVBResponse;
import ru.spiiran.sdt_server.application.dto.selection.DtoSelectionResponse;
import ru.spiiran.sdt_server.application.dto.selection.DtoTimeRegion;
import ru.spiiran.sdt_server.domain.service.VBOptimizationService;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class VBOptimizationServiceImpl implements VBOptimizationService {

    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 100;
    private static final double CROSSOVER_RATE = 0.8;
    private static final double MUTATION_RATE = 0.02;
    private static final int TOURNAMENT_SIZE = 2;

    private Integer numberOfSatellites = null; // Устанавливается через сеттер

    @Override
    public List<DtoVBResponse> multicriteriaOptimization(List<DtoSelectionResponse> selectionResponses, Integer numberOfSatellites) {
        this.numberOfSatellites = numberOfSatellites;
        int N = selectionResponses.size();
        if (N == 0) return Collections.emptyList();
        Random random = new Random();

        // 1. Генерация начальной популяции
        List<boolean[]> population = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE; i++) {
            boolean[] ind = generateIndividual(N, numberOfSatellites, random);
            if (numberOfSatellites != null) fixK(ind, numberOfSatellites, random);
            population.add(ind);
        }

        // 2. Основной цикл NSGA-II
        for (int g = 0; g < GENERATIONS; g++) {
            List<Individual> evaluated = population.stream()
                    .map(ind -> evaluate(ind, selectionResponses, numberOfSatellites))
                    .collect(Collectors.toList());

            List<List<Individual>> fronts = fastNonDominatedSort(evaluated);
            for (int i = 0; i < fronts.size(); i++) {
                for (Individual ind : fronts.get(i)) {
                    ind.frontRank = i;
                }
                assignCrowdingDistance(fronts.get(i), numberOfSatellites != null);
            }

            List<boolean[]> matingPool = new ArrayList<>();
            while (matingPool.size() < POPULATION_SIZE) {
                Individual ind = tournamentSelect(evaluated, random);
                matingPool.add(Arrays.copyOf(ind.x, ind.x.length));
            }

            List<boolean[]> offspring = new ArrayList<>();
            while (offspring.size() < POPULATION_SIZE) {
                boolean[] parent1 = matingPool.get(random.nextInt(matingPool.size()));
                boolean[] parent2 = matingPool.get(random.nextInt(matingPool.size()));
                boolean[] child1 = Arrays.copyOf(parent1, N);
                boolean[] child2 = Arrays.copyOf(parent2, N);

                if (random.nextDouble() < CROSSOVER_RATE) {
                    int point = random.nextInt(N);
                    for (int j = 0; j < N; j++) {
                        if (j < point) {
                            child1[j] = parent1[j];
                            child2[j] = parent2[j];
                        } else {
                            child1[j] = parent2[j];
                            child2[j] = parent1[j];
                        }
                    }
                }

                mutate(child1, numberOfSatellites, random);
                mutate(child2, numberOfSatellites, random);

                if (numberOfSatellites != null) {
                    fixK(child1, numberOfSatellites, random);
                    fixK(child2, numberOfSatellites, random);
                }
                if (offspring.size() < POPULATION_SIZE) offspring.add(child1);
                if (offspring.size() < POPULATION_SIZE) offspring.add(child2);
            }

            // Новое поколение: объединяем, сортируем, выбираем лучших
            List<boolean[]> union = new ArrayList<>();
            union.addAll(population);
            union.addAll(offspring);
            List<Individual> unionEvaluated = union.stream()
                    .map(ind -> evaluate(ind, selectionResponses, numberOfSatellites))
                    .collect(Collectors.toList());
            List<List<Individual>> unionFronts = fastNonDominatedSort(unionEvaluated);
            for (int i = 0; i < unionFronts.size(); i++) {
                for (Individual ind : unionFronts.get(i)) {
                    ind.frontRank = i;
                }
                assignCrowdingDistance(unionFronts.get(i), numberOfSatellites != null);
            }
            List<boolean[]> nextGen = new ArrayList<>();
            int frontIndex = 0;
            while (frontIndex < unionFronts.size() && nextGen.size() + unionFronts.get(frontIndex).size() <= POPULATION_SIZE) {
                for (Individual ind : unionFronts.get(frontIndex)) {
                    nextGen.add(Arrays.copyOf(ind.x, N));
                }
                frontIndex++;
            }
            if (nextGen.size() < POPULATION_SIZE && frontIndex < unionFronts.size()) {
                List<Individual> lastFront = unionFronts.get(frontIndex);
                assignCrowdingDistance(lastFront, numberOfSatellites != null);
                lastFront.sort((a, b) -> -Double.compare(a.crowding, b.crowding));
                for (Individual ind : lastFront) {
                    if (nextGen.size() < POPULATION_SIZE) {
                        nextGen.add(Arrays.copyOf(ind.x, N));
                    }
                }
            }
            population = nextGen;
        }

        // Финальный Парето-фронт
        List<Individual> finalPopulation = population.stream()
                .map(ind -> evaluate(ind, selectionResponses, numberOfSatellites))
                .collect(Collectors.toList());
        List<List<Individual>> fronts = fastNonDominatedSort(finalPopulation);
        if (fronts.isEmpty()) return Collections.emptyList();
        for (int i = 0; i < fronts.size(); i++) {
            for (Individual ind : fronts.get(i)) {
                ind.frontRank = i;
            }
            assignCrowdingDistance(fronts.get(i), numberOfSatellites != null);
        }
        List<Individual> paretoFront = fronts.get(0);

        // Фильтрация пустых решений (нет выбранных КА)
        List<Individual> nonEmpty = paretoFront.stream()
                .filter(ind -> ind.count > 0)
                .collect(Collectors.toList());
        Individual best;
        if (nonEmpty.isEmpty()) {
            // Фолбэк: выдаем лучшую по coverage среди всех
            best = finalPopulation.stream()
                    .max(Comparator.comparingLong(ind -> ind.coverage))
                    .orElse(paretoFront.get(0));
        } else {
            best = selectBalanced(nonEmpty, numberOfSatellites != null);
        }

        List<DtoVBResponse> result = new ArrayList<>();
        for (int i = 0; i < best.x.length; i++) {
            if (best.x[i]) {
                result.add(selectionResponses.get(i).getPreFiltrationResponse());
            }
        }
        return result;
    }

    private boolean[] generateIndividual(int N, Integer K, Random random) {
        boolean[] x = new boolean[N];
        if (K == null) {
            for (int i = 0; i < N; i++) x[i] = random.nextBoolean();
        } else {
            if (K > N) K = N;
            if (K < 0) K = 0;
            List<Integer> indices = IntStream.range(0, N).boxed().collect(Collectors.toList());
            Collections.shuffle(indices, random);
            for (int i = 0; i < K; i++) x[indices.get(i)] = true;
        }
        return x;
    }

    private void fixK(boolean[] x, int K, Random random) {
        List<Integer> ones = new ArrayList<>();
        List<Integer> zeros = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            if (x[i]) ones.add(i);
            else zeros.add(i);
        }
        while (ones.size() > K) {
            int idx = ones.remove(random.nextInt(ones.size()));
            x[idx] = false;
            zeros.add(idx);
        }
        while (ones.size() < K && !zeros.isEmpty()) {
            int idx = zeros.remove(random.nextInt(zeros.size()));
            x[idx] = true;
            ones.add(idx);
        }
    }

    private void mutate(boolean[] x, Integer K, Random random) {
        if (K == null) {
            for (int i = 0; i < x.length; i++) {
                if (random.nextDouble() < MUTATION_RATE) {
                    x[i] = !x[i];
                }
            }
        } else {
            int swaps = 1 + random.nextInt(2);
            for (int s = 0; s < swaps; s++) {
                List<Integer> ones = new ArrayList<>();
                List<Integer> zeros = new ArrayList<>();
                for (int i = 0; i < x.length; i++) {
                    if (x[i]) ones.add(i);
                    else zeros.add(i);
                }
                if (!ones.isEmpty() && !zeros.isEmpty()) {
                    int idx1 = ones.get(random.nextInt(ones.size()));
                    int idx0 = zeros.get(random.nextInt(zeros.size()));
                    x[idx1] = false;
                    x[idx0] = true;
                }
            }
        }
    }

    private Individual evaluate(boolean[] x, List<DtoSelectionResponse> sel, Integer K) {
        long coverage = 0;
        long maxRevisit = 0;
        int count = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i]) {
                DtoTimeRegion tr = sel.get(i).getTimeRegion();
                if (tr != null) {
                    coverage += (tr.getSeesRegion() != null ? tr.getSeesRegion() : 0L);
                    maxRevisit = Math.max(maxRevisit, (tr.getNotSeeRegion() != null ? tr.getNotSeeRegion() : 0L));
                }
                count++;
            }
        }
        double penalty = 0.0;
        if (K != null && count != K) penalty = Double.POSITIVE_INFINITY;
        return new Individual(x, coverage, maxRevisit, count, penalty);
    }

    private List<List<Individual>> fastNonDominatedSort(List<Individual> individuals) {
        List<List<Individual>> fronts = new ArrayList<>();
        Map<Individual, List<Individual>> S = new HashMap<>();
        Map<Individual, Integer> n = new HashMap<>();
        List<Individual> firstFront = new ArrayList<>();

        for (Individual p : individuals) {
            S.put(p, new ArrayList<>());
            n.put(p, 0);
            for (Individual q : individuals) {
                if (dominates(p, q)) {
                    S.get(p).add(q);
                } else if (dominates(q, p)) {
                    n.put(p, n.get(p) + 1);
                }
            }
            if (n.get(p) == 0) firstFront.add(p);
        }
        fronts.add(firstFront);
        int i = 0;
        while (!fronts.get(i).isEmpty()) {
            List<Individual> nextFront = new ArrayList<>();
            for (Individual p : fronts.get(i)) {
                for (Individual q : S.get(p)) {
                    n.put(q, n.get(q) - 1);
                    if (n.get(q) == 0) nextFront.add(q);
                }
            }
            i++;
            fronts.add(nextFront);
        }
        fronts.removeIf(List::isEmpty);
        return fronts;
    }

    private boolean dominates(Individual a, Individual b) {
        if (a.penalty > 0 || b.penalty > 0) {
            if (a.penalty == b.penalty) return false;
            return a.penalty < b.penalty;
        }
        boolean twoObjectives = (numberOfSatellites != null);
        if (twoObjectives) {
            boolean betterOrEqual = (a.coverage >= b.coverage) && (a.maxRevisit <= b.maxRevisit);
            boolean strictlyBetter = (a.coverage > b.coverage) || (a.maxRevisit < b.maxRevisit);
            return betterOrEqual && strictlyBetter;
        } else {
            boolean betterOrEqual = (a.coverage >= b.coverage) && (a.maxRevisit <= b.maxRevisit) && (a.count <= b.count);
            boolean strictlyBetter = (a.coverage > b.coverage) || (a.maxRevisit < b.maxRevisit) || (a.count < b.count);
            return betterOrEqual && strictlyBetter;
        }
    }

    private void assignCrowdingDistance(List<Individual> front, boolean twoObjectives) {
        int n = front.size();
        if (n == 0) return;
        for (Individual ind : front) ind.crowding = 0.0;

        double eps = 1e-6;
        // f1: coverage (max)
        List<Individual> sorted = new ArrayList<>(front);
        sorted.sort(Comparator.comparingLong(ind -> ind.coverage));
        sorted.get(0).crowding = Double.POSITIVE_INFINITY;
        sorted.get(n - 1).crowding = Double.POSITIVE_INFINITY;
        long minF1 = sorted.get(0).coverage, maxF1 = sorted.get(n - 1).coverage;
        double denomF1 = maxF1 - minF1;
        for (int i = 1; i < n - 1; i++) {
            if (denomF1 > eps)
                sorted.get(i).crowding += (sorted.get(i + 1).coverage - sorted.get(i - 1).coverage) / (denomF1 + eps);
        }
        // f2: maxRevisit (min)
        sorted = new ArrayList<>(front);
        sorted.sort(Comparator.comparingLong(ind -> ind.maxRevisit));
        sorted.get(0).crowding = Double.POSITIVE_INFINITY;
        sorted.get(n - 1).crowding = Double.POSITIVE_INFINITY;
        long minF2 = sorted.get(0).maxRevisit, maxF2 = sorted.get(n - 1).maxRevisit;
        double denomF2 = maxF2 - minF2;
        for (int i = 1; i < n - 1; i++) {
            if (denomF2 > eps)
                sorted.get(i).crowding += (sorted.get(i + 1).maxRevisit - sorted.get(i - 1).maxRevisit) / (denomF2 + eps);
        }
        if (!twoObjectives) {
            // f3: count (min)
            sorted = new ArrayList<>(front);
            sorted.sort(Comparator.comparingInt(ind -> ind.count));
            sorted.get(0).crowding = Double.POSITIVE_INFINITY;
            sorted.get(n - 1).crowding = Double.POSITIVE_INFINITY;
            int minF3 = sorted.get(0).count, maxF3 = sorted.get(n - 1).count;
            double denomF3 = maxF3 - minF3;
            for (int i = 1; i < n - 1; i++) {
                if (denomF3 > eps)
                    sorted.get(i).crowding += (double) (sorted.get(i + 1).count - sorted.get(i - 1).count) / (denomF3 + eps);
            }
        }
    }

    private Individual tournamentSelect(List<Individual> pop, Random random) {
        Individual best = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Individual ind = pop.get(random.nextInt(pop.size()));
            if (best == null || better(ind, best)) best = ind;
        }
        return best;
    }

    private boolean better(Individual a, Individual b) {
        if (a.penalty < b.penalty) return true;
        if (a.penalty > b.penalty) return false;
        if (a.frontRank < b.frontRank) return true;
        if (a.frontRank > b.frontRank) return false;
        return a.crowding > b.crowding;
    }

    private Individual selectBalanced(List<Individual> paretoFront, boolean twoObjectives) {
        if (paretoFront.isEmpty()) throw new IllegalArgumentException("Pareto front is empty");
        double eps = 1e-6;
        long maxCov = paretoFront.stream().mapToLong(ind -> ind.coverage).max().orElse(1);
        long minCov = paretoFront.stream().mapToLong(ind -> ind.coverage).min().orElse(0);
        long maxRev = paretoFront.stream().mapToLong(ind -> ind.maxRevisit).max().orElse(1);
        long minRev = paretoFront.stream().mapToLong(ind -> ind.maxRevisit).min().orElse(0);
        int maxCnt = paretoFront.stream().mapToInt(ind -> ind.count).max().orElse(1);
        int minCnt = paretoFront.stream().mapToInt(ind -> ind.count).min().orElse(0);

        Individual best = paretoFront.get(0);
        double bestScore = Double.POSITIVE_INFINITY;
        for (Individual ind : paretoFront) {
            double normCov = (double) (maxCov - ind.coverage) / (maxCov - minCov + eps); // maximize
            double normRev = (double) (ind.maxRevisit - minRev) / (maxRev - minRev + eps); // minimize
            double normCnt = (double) (ind.count - minCnt) / (maxCnt - minCnt + eps); // minimize
            double score = normCov + normRev + (twoObjectives ? 0.0 : normCnt);
            if (score < bestScore) {
                bestScore = score;
                best = ind;
            }
        }
        return best;
    }

    static class Individual {
        boolean[] x;
        long coverage;      // f1 (max)
        long maxRevisit;    // f2 (min)
        int count;          // f3 (min)
        double penalty;
        double crowding = 0.0;
        int frontRank = 0;

        Individual(boolean[] x, long coverage, long maxRevisit, int count, double penalty) {
            this.x = Arrays.copyOf(x, x.length);
            this.coverage = coverage;
            this.maxRevisit = maxRevisit;
            this.count = count;
            this.penalty = penalty;
        }
    }
}