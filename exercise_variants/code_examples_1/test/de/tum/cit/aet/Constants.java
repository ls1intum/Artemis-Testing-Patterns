package de.tum.cit.aet;

public class Constants {

    public enum Variant{
        ZOO,
        ICE,
        TROPICAL
    }

    public static Variant variant = Variant.ZOO;
    public static String initialControlMode(){
        return switch (variant) {
            case ZOO -> "off";
            case ICE -> "top";
            case TROPICAL -> "neutral";
        };
    }

    public static String environmentAggregationClassPath() {
        return switch (variant) {
            case ZOO -> "de.tum.cit.aet.Zoo";
            case ICE -> "de.tum.cit.aet.IceHockeyLeague";
            case TROPICAL -> "de.tum.cit.aet.TropicalParadise";
        };
    }

    public static String environmentClassPath() {
        return switch (variant) {
            case ZOO -> "de.tum.cit.aet.House";
            case ICE -> "de.tum.cit.aet.IceHockeyTeam";
            case TROPICAL -> "de.tum.cit.aet.TropicalZone";
        };
    }

    public static String componentClassPath() {
        return switch (variant) {
            case ZOO -> "de.tum.cit.aet.Sensor";
            case ICE -> "de.tum.cit.aet.Puck";
            case TROPICAL -> "de.tum.cit.aet.Pool";
        };
    }

    public static String environmentChangeControlMode() {
        return switch (variant) {
            case ZOO -> "changeHeatingLevel";
            case ICE -> "changeLeaguePosition";
            case TROPICAL -> "changeClimateControlMode";
        };
    }

    public static String environmentComputeAverageCondition() {
        return switch (variant) {
            case ZOO -> "calculateAvgHouseTemperature";
            case ICE -> "computeAvgPuckGoalDifferential";
            case TROPICAL -> "computeAvgPoolWaterQuality";
        };
    }

    public static String environmentAggregationComputeMaxAverage() {
        return switch (variant) {
            case ZOO -> "calculateMaxAvgHouseTemperature";
            case ICE -> "calculateMaxAvgTeamGoalDifferential";
            case TROPICAL -> "calculateMaxAvgPoolWaterQuality";
        };
    }

    public static String intToLevel(int level) {
        return switch (variant) {
            case ZOO -> switch (level) {
                case 1 -> "low";
                case 2 -> "high";
                default -> "off";
            };
            case ICE -> switch (level){
                case 1 -> "top";
                case 2 -> "middle";
                default -> "bottom";
            };
            case TROPICAL -> switch (level){
                case 1 -> "humid";
                case 2 -> "dry";
                default -> "neutral";
            };
        };
    }

    public static String environmentAttributeName() {
        return switch (variant) {
            case ZOO -> "heatingLevel";
            case ICE -> "leaguePosition";
            case TROPICAL -> "climateControlMode";
        };
    }

    public static String componentObject() {
        return switch (variant) {
            case ZOO -> "sensors";
            case ICE -> "pucks";
            case TROPICAL -> "pools";
        };
    }

    public static String environmentObject() {
        return switch (variant) {
            case ZOO -> "houses";
            case ICE -> "teams";
            case TROPICAL -> "zones";
        };
    }
}
