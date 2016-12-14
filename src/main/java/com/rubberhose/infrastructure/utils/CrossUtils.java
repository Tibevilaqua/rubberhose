package com.rubberhose.infrastructure.utils;

import com.rubberhose.endpoint.cross.CrossDTO;
import com.rubberhose.infrastructure.LaneEnum;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

/**
 * Created by root on 10/12/16.
 */
public class CrossUtils {


    private static final String MACHINE_VALUE_PATTERN = "^(?!([A-Z][0-9]{1,10})).*$";

    /**
     * Return only the mills (sorted)
     * From: A303030
     * To:   303030
     */
    public static List<Integer> getMillsFrom(List<String> crosses){
        return  crosses.stream().map(CrossUtils::getCrossMills).sorted().collect(Collectors.toList());
    }

    /**
     * Return only the mills (sorted)
     * From: A303030
     * To:   303030
     */
    public static List<Integer> getMillsFrom(List<String> crosses, LaneEnum laneEnum){

        List<String> result = laneEnum == LaneEnum.NORTHBOUND ? getNorthLaneCrosses(crosses) : getSouthLaneCrosses(crosses);

        return  result.stream().map(CrossUtils::getCrossMills).sorted().collect(Collectors.toList());
    }


    private static boolean isCrossOnThe(String cross, LaneEnum laneEnum){
        return laneEnum.getValue().equals(getLanePrefix(cross));
    }

    /**
     * True if the cross values do not follow the patten (One Letter + up to 10 Numbers).
     * False otherwise.
     */
    public static boolean isListInaccurate(CrossDTO crossDTO){
        return crossDTO.getCrosses().stream().anyMatch(eachCross -> Objects.isNull(eachCross) || eachCross.length() > 11 || eachCross.matches(CrossUtils.MACHINE_VALUE_PATTERN));
    }

    /**
     * Return the whole number of accurate crosses (following the pattern)
     * @param crossDTO
     * @return
     */
    public static int getNumberOfValidCrosses(CrossDTO crossDTO){

        boolean isNorthLaneUsed = isLaneUsed(crossDTO.getCrosses(), LaneEnum.NORTHBOUND);
        boolean isSouthLaneUsed = isLaneUsed(crossDTO.getCrosses(), LaneEnum.SOUTHBOUND);

        Integer numberOfCrossesAfterTreatment = 0;

        if(isNorthLaneUsed){
            numberOfCrossesAfterTreatment+= getNorthLaneCrosses(crossDTO.getCrosses()).size();
        }
        if(isSouthLaneUsed){
            numberOfCrossesAfterTreatment += getSouthLaneCrosses(crossDTO.getCrosses()).size();
        }

        return numberOfCrossesAfterTreatment;

    }


    public static Integer getNumberOfCarsInBetween(Integer beginningCurrentPeriodInMills, Integer limitCurrentPeriodInMills, List<Integer> crossCollectionsMills){

        return (int) crossCollectionsMills.stream().sequential().filter(cross -> cross >= beginningCurrentPeriodInMills && cross < limitCurrentPeriodInMills).count();


    }


    public static Integer dividePer(Integer division, Integer numberOfCrosses){
        return new BigDecimal(numberOfCrosses).divide(new BigDecimal(division),BigDecimal.ROUND_HALF_UP).intValue();
    }


    /**
     * Returns true if the lane sent as a parameter is crosses at least one. <br>
     *     Patterns:
     *     Lane A (NorthBound), must have two crosses with the prefix "A" in sequence e.g "A900000" && "A900150"</br>
     *     Lane b (SouthBound), must have four crosses with the prefix "B" and "A" respectively in sequence e.g "B900000" && "A900075" && "B900150" && "A900225"
     */
    public static BiPredicate<List<String>, LaneEnum> isLaneUsed = CrossUtils::isLaneUsed;

    private static boolean isLaneUsed(List<String> crosses, LaneEnum laneEnum){

        return LaneEnum.NORTHBOUND == laneEnum ? !getNorthLaneCrosses(crosses).isEmpty() : !getSouthLaneCrosses(crosses).isEmpty();
    }

    private static String getLanePrefix(String cross){
        return cross.substring(0,1);
    }

    private static Integer getCrossMills(String cross){
        return Integer.valueOf(cross.substring(1,cross.length()));
    }

    /**
     * Get all the crosses which follow the north lane pattern: e.g A268981, A269123
     */
    private static List<String> getNorthLaneCrosses(List<String> crosses) {
        List<String> result = new ArrayList<>();


            boolean jumpNextIteration = false;

            for (int i = 0; i < crosses.size() - 1; i++) {

                if (jumpNextIteration) {
                    jumpNextIteration = false;
                    continue;
                }

                //Verify North lane pattern (2 consecutive crosses starting with "A")
                if (isCrossOnThe(crosses.get(i), LaneEnum.NORTHBOUND)) {
                    if (isCrossOnThe(crosses.get(i + 1), LaneEnum.NORTHBOUND)) {
                        result.add(crosses.get(i));
                        result.add(crosses.get(i + 1));
                        jumpNextIteration = true;
                    }
                }
            }

        return result;
    }

    /**
     * Get all the crosses which follow the south lane pattern: e.g A604957, B604960, A605128, B605132
     */
    private static List<String> getSouthLaneCrosses(List<String> crosses){
        List<String> result = new ArrayList<>();
        int jumpFollowingIterations = 0;
        for (int i = 0; i < crosses.size() -3; i++) {

            if(jumpFollowingIterations > 0){
                jumpFollowingIterations--;
                continue;
            }

            //Verify South lane pattern (4 consecutive crosses with the following pattern "A", "B", "A", "B")
            if(isCrossOnThe(crosses.get(i),LaneEnum.NORTHBOUND)){
                if(isCrossOnThe(crosses.get(i+1),LaneEnum.SOUTHBOUND)){
                    if(isCrossOnThe(crosses.get(i+2),LaneEnum.NORTHBOUND)){
                        if(isCrossOnThe(crosses.get(i+3),LaneEnum.SOUTHBOUND)){
                            result.addAll(Arrays.asList(crosses.get(i),crosses.get(i+1),crosses.get(i+2),crosses.get(i+3)));
                            jumpFollowingIterations = 3;
                        }
                    }
                }
            }
        }
        return result;
    }


}
