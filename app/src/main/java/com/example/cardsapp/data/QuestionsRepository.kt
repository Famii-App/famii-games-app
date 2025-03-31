package com.example.cardsapp.data

object QuestionsRepository {
    val questions = listOf(
        Question("¿Cuál fue tu primera impresión de tu pareja?"),
        Question("¿Qué es lo que más te gusta de tu relación?"),
        Question("¿Cuál ha sido el momento más especial que han compartido?"),
        Question("¿Qué es lo que más admiras de tu pareja?"),
        Question("¿Cuál es tu recuerdo favorito juntos?"),
        Question("¿Qué te hizo enamorarte de tu pareja?"),
        Question("¿Cuál ha sido el mejor regalo que te ha dado?"),
        Question("¿Qué es lo que más te hace reír de tu pareja?"),
        Question("¿Cuál es el lugar favorito al que han ido juntos?"),
        Question("¿Qué es lo que más te sorprende de tu pareja?")
    ).shuffled()
} 