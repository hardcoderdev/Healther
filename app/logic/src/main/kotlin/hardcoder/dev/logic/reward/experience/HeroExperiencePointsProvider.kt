package hardcoder.dev.logic.reward.experience

class HeroExperiencePointsProvider {

    fun getExperiencePointsToNextLevel(currentLevel: Int): Float {
        return when (currentLevel) {
            1 -> LEVEL_1_XP_NEED
            2 -> LEVEL_2_XP_NEED
            3 -> LEVEL_3_XP_NEED
            4 -> LEVEL_4_XP_NEED
            5 -> LEVEL_5_XP_NEED
            6 -> LEVEL_6_XP_NEED
            7 -> LEVEL_7_XP_NEED
            8 -> LEVEL_8_XP_NEED
            9 -> LEVEL_9_XP_NEED
            10 -> LEVEL_10_XP_NEED
            11 -> LEVEL_11_XP_NEED
            12 -> LEVEL_12_XP_NEED
            13 -> LEVEL_13_XP_NEED
            14 -> LEVEL_14_XP_NEED
            15 -> LEVEL_15_XP_NEED
            16 -> LEVEL_16_XP_NEED
            17 -> LEVEL_17_XP_NEED
            18 -> LEVEL_18_XP_NEED
            19 -> LEVEL_19_XP_NEED
            20 -> LEVEL_20_XP_NEED
            21 -> LEVEL_21_XP_NEED
            22 -> LEVEL_22_XP_NEED
            23 -> LEVEL_23_XP_NEED
            24 -> LEVEL_24_XP_NEED
            25 -> LEVEL_25_XP_NEED
            26 -> LEVEL_26_XP_NEED
            27 -> LEVEL_27_XP_NEED
            28 -> LEVEL_28_XP_NEED
            29 -> LEVEL_29_XP_NEED
            30 -> LEVEL_30_XP_NEED
            31 -> LEVEL_31_XP_NEED
            32 -> LEVEL_32_XP_NEED
            33 -> LEVEL_33_XP_NEED
            34 -> LEVEL_34_XP_NEED
            35 -> LEVEL_35_XP_NEED
            36 -> LEVEL_36_XP_NEED
            37 -> LEVEL_37_XP_NEED
            38 -> LEVEL_38_XP_NEED
            39 -> LEVEL_39_XP_NEED
            40 -> LEVEL_40_XP_NEED
            41 -> LEVEL_41_XP_NEED
            42 -> LEVEL_42_XP_NEED
            43 -> LEVEL_43_XP_NEED
            44 -> LEVEL_44_XP_NEED
            45 -> LEVEL_45_XP_NEED
            46 -> LEVEL_46_XP_NEED
            47 -> LEVEL_47_XP_NEED
            48 -> LEVEL_48_XP_NEED
            49 -> LEVEL_49_XP_NEED
            50 -> LEVEL_50_XP_NEED
            else -> LEVEL_50_XP_NEED
        }
    }
}