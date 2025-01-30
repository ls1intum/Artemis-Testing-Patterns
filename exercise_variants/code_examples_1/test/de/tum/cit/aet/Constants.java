package de.tum.cit.aet;

public class Constants {

    public static int variant = 3;
    public static String initialControlMode(int version){
        return switch (version) {
            case 1 -> "off";
            case 2 -> "top";
            case 3 -> "neutral";
            default -> "";
        };
    }

    public static String environmentAggregationClassPath(int version) {
        return switch (version) {
            case 1 -> "de.tum.cit.aet.Zoo";
            case 2 -> "de.tum.cit.aet.IceHockeyLeague";
            case 3 -> "de.tum.cit.aet.TropicalParadise";
            default -> "";
        };
    }

    public static String environmentClassPath(int version) {
        return switch (version) {
            case 1 -> "de.tum.cit.aet.House";
            case 2 -> "de.tum.cit.aet.IceHockeyTeam";
            case 3 -> "de.tum.cit.aet.TropicalZone";
            default -> "";
        };
    }

    public static String componentClassPath(int version) {
        return switch (version) {
            case 1 -> "de.tum.cit.aet.Sensor";
            case 2 -> "de.tum.cit.aet.Puck";
            case 3 -> "de.tum.cit.aet.Pool";
            default -> ""
        };
    }

    public static String environmentChangeControlMode(int version) {
        return switch (version) {
            case 1 -> "changeHeatingLevel";
            case 2 -> "changeLeaguePosition";
            case 3 -> "changeClimateControlMode";
            default -> "";
        };
    }

    public static String environmentComputeAverageCondition(int version) {
        return switch (version) {
            case 1 -> "calculateAvgHouseTemperature";
            case 2 -> "computeAvgPuckGoalDifferential";
            case 3 -> "computeAvgPoolWaterQuality";
            default -> "";
        };
    }

    public static String environmentAggregationComputeMaxAverage(int version) {
        return switch (version) {
            case 1 -> "calculateMaxAvgHouseTemperature";
            case 2 -> "calculateMaxAvgTeamGoalDifferential";
            case 3 -> "calculateMaxAvgPoolWaterQuality";
            default -> "";
        };
    }

    public static String intToLevel(int version, int level) {
        return switch (version) {
            case 1 -> switch (level) {
                case 1 -> "low";
                case 2 -> "high";
                case 3 -> "off";
                default -> "";
            };
            case 2 -> switch (level){
                case 1 -> "top";
                case 2 -> "middle";
                case 3 -> "bottom";
                default -> "";
            };
            default -> switch (level){
                case 1 -> "humid";
                case 2 -> "dry";
                case 3 -> "neutral";
                default -> "";
            }; // Return an empty string for any other version
        };
    }

    public static String environmentAttributeName(int version) {
        return switch (version) {
            case 1 -> "heatingLevel";
            case 2 -> "leaguePosition";
            case 3 -> "climateControlMode";
            default -> "";
        };
    }

    public static String componentObject(int version) {
        return switch (version) {
            case 1 -> "sensors";
            case 2 -> "pucks";
            case 3 -> "pools";
            default -> "";
        };
    }

    public static String environmentObject(int version) {
        return switch (version) {
            case 1 -> "houses";
            case 2 -> "teams";
            case 3 -> "zones";
            default -> "";
        };
    }
}
