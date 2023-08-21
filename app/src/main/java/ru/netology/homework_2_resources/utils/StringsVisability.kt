package ru.netology.homework_2_resources.utils

object StringsVisability { /* объявляем синглтон - объект в единственном экземпляре */

    fun getCoolNumeralString(num:Long):String = when(num) {
        in 0..999 -> num.toString()
        in 1_000..9_999 -> {"${num/1000} ${if(num/100%10>0){".${num%1000/100}"}else{""}} K"}
        in 10_000..999_999 -> {"${num/1000}  K"}
        in 1_000_000..9_999_999 -> {"${num/1_000_000} ${if(num/100_000%10>0){".${num%1_000_000/100_000}"}else{""}} M"}
        in 10_000_000..999_999_999 -> {"${num/1_000_000}  M"}
        else -> {"${num/1000_000_000} ${if(num/100_000_000%10>0){".${num%1000_000_000/100_000_000}"}else{""}} B"}
    }
}