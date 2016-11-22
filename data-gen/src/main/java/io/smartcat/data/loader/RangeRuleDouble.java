package io.smartcat.data.loader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Rule for creating random range values.
 */
public class RangeRuleDouble implements Rule<Double> {

    private boolean exclusive;

    // definition of the range: e.g [a,b,c,d] : a < b <= c < d is a set of ranges: {[a,b),[c,d)}
    private List<Double> ranges = new ArrayList<>();

    private RangeRuleDouble() {
    };

    public static RangeRuleDouble withRanges(Double... rangeMarkers) {
        RangeRuleDouble result = new RangeRuleDouble();

        result.ranges.addAll(Arrays.asList(rangeMarkers));

        return result;
    }

    public static RangeRuleDouble withRangesX(Double... rangeMarkers) {
        RangeRuleDouble result = new RangeRuleDouble();

        result.exclusive = true;
        result.ranges.addAll(Arrays.asList(rangeMarkers));

        return result;
    }

    public static RangeRuleDouble withRanges(List<Double> rangeMarkers) {
        RangeRuleDouble result = new RangeRuleDouble();

        result.ranges.addAll(rangeMarkers);

        return result;
    }

    @Override
    public boolean isExclusive() {
        return this.exclusive;
    }

    private List<Double> getAllowedRanges() {
        return ranges;
    }

    @Override
    public Rule<Double> recalculatePrecedance(Rule<Double> exclusiveRule) {
        if (!exclusiveRule.isExclusive()) {
            throw new IllegalArgumentException("no need to calculate rule precedance with non exclusive rule");
        }
        if (!(exclusiveRule instanceof RangeRuleDouble)) {
            throw new IllegalArgumentException("cannot compare discrete and range rules");
        }
        RangeRuleDouble otherRule = (RangeRuleDouble) exclusiveRule;

        if (!RangeUtil.rangesIntersects(this.ranges, otherRule.getAllowedRanges())) {
            return this;
        }
        List<Double> newRanges = RangeUtil.recalculateRanges(this.ranges, otherRule.getAllowedRanges());

        return RangeRuleDouble.withRanges(newRanges);
    }

    @Override
    public Double getRandomAllowedValue() {
        // ranges = [a,b,c,d]
        // =>
        // (a,b],(c,d]
        // 0 , 1
        int randomRangeIndex = 0;
        if (ranges.size() > 2) {
            randomRangeIndex = ThreadLocalRandom.current().nextInt(0, ranges.size() / 2);
        }
        System.out.println("size is: " + ranges.size());
        System.out.println("randomRangeIdex is: " + randomRangeIndex);

        // randomRangeIndex == 0 => index1 = 0, index2 = 1;
        // randomRangeIndex == 1 => index1 = 2, index2 = 3;
        // randomRangeIndex == 2 => index1 = 4, index2 = 5;
        // randomRangeIndex == 3 => index1 = 6, index2 = 7;
        Double randomBirthDate = ThreadLocalRandom.current().nextDouble(ranges.get(randomRangeIndex * 2),
                ranges.get((randomRangeIndex * 2) + 1));
        // if we used generic type <T> instead of Long there would be no way to get random of type T because we do not
        // know what is the type T

        return randomBirthDate;
    }

}