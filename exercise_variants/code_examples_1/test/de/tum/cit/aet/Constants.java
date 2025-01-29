package de.tum.cit.aet;

public class Constants {

    public static int variant = 3;
    public static String initialControlMode(int version){
        return switch (version) {
            case 1 -> "off";
            case 2 -> "top";
            default -> "neutral";
        };
    }

    public static String environmentAggregationClassPath(int version) {
        return switch (version) {
            case 1 -> "de.tum.cit.aet.Zoo";
            case 2 -> "de.tum.cit.aet.IceHockeyLeague";
            default -> "de.tum.cit.aet.TropicalParadise";
        };
    }

    public static String environmentClassPath(int version) {
        return switch (version) {
            case 1 -> "de.tum.cit.aet.House";
            case 2 -> "de.tum.cit.aet.IceHockeyTeam";
            default -> "de.tum.cit.aet.TropicalZone";
        };
    }

    public static String componentClassPath(int version) {
        return switch (version) {
            case 1 -> "de.tum.cit.aet.Sensor";
            case 2 -> "de.tum.cit.aet.Puck";
            default -> "de.tum.cit.aet.Pool";
        };
    }

    public static String environmentChangeControlMode(int version) {
        return switch (version) {
            case 1 -> "changeHeatingLevel";
            case 2 -> "changeLeaguePosition";
            default -> "changeClimateControlMode";
        };
    }

    public static String environmentComputeAverageCondition(int version) {
        return switch (version) {
            case 1 -> "calculateAvgHouseTemperature";
            case 2 -> "computeAvgPuckGoalDifferential";
            default -> "computeAvgPoolWaterQuality";
        };
    }

    public static String environmentAggregationComputeMaxAverage(int version) {
        return switch (version) {
            case 1 -> "calculateMaxAvgHouseTemperature";
            case 2 -> "calculateMaxAvgTeamGoalDifferential";
            default -> "calculateMaxAvgPoolWaterQuality";
        };
    }

    public static String intToLevel(int version, int level) {
        return switch (version) {
            case 1 -> switch (level) {
                case 1 -> "low";
                case 2 -> "high";
                default -> "off";
            };
            case 2 -> switch (level){
                case 1 -> "top";
                case 2 -> "middle";
                default -> "bottom";
            };
            default -> switch (level){
                case 1 -> "humid";
                case 2 -> "dry";
                default -> "neutral";
            }; // Return an empty string for any other version
        };
    }

    public static String environmentAttributeName(int version) {
        return switch (version) {
            case 1 -> "heatingLevel";
            case 2 -> "leaguePosition";
            default -> "climateControlMode";
        };
    }

    public static String componentObject(int version) {
        return switch (version) {
            case 1 -> "sensors";
            case 2 -> "pucks";
            default -> "pools";
        };
    }

    public static String environmentObject(int version) {
        return switch (version) {
            case 1 -> "houses";
            case 2 -> "teams";
            default -> "zones";
        };
    }
}
