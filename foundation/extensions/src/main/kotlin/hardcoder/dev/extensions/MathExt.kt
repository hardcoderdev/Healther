package hardcoder.dev.extensions

inline infix fun <reified T : Number> Number.safeDiv(value: Number): T {
    return when (T::class) {
        Int::class -> {
            val value1 = this.toInt()
            val value2 = value.toInt()
            if (value2 == 0) 0
            else value1 / value2
        }
        Long::class -> {
            val value1 = this.toLong()
            val value2 = value.toLong()
            if (value2 == 0L) 0L
            else value1 / value2
        }
        Float::class -> {
            val value1 = this.toFloat()
            val value2 = value.toFloat()
            if (value2 == 0f) 0f
            else value1 / value2
        }
        Double::class -> {
            val value1 = this.toDouble()
            val value2 = value.toDouble()
            if (value2 == 0.0) 0.0
            else value1 / value2
        }
        else -> error("Unexpected type: ${T::class}")
    } as T
}