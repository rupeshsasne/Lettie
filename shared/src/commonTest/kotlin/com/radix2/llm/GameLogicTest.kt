package com.radix2.llm

import com.radix2.llm.data.WordData
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.WordMatching
import com.radix2.llm.game.GameSession
import com.radix2.llm.game.QuizGenerator
import com.radix2.llm.game.SubmitResult
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class GameLogicTest {

    private val repo = WordRepository()

    @Test
    fun lastLetterIsComputedIgnoringSpaces() {
        val newYork = assertNotNull(repo.byId("city_newyork"))
        assertEquals('N', newYork.firstLetter)
        assertEquals('K', newYork.lastLetter)
    }

    @Test
    fun eggplantDoesNotCollideWithElephant() {
        assertTrue(WordMatching.matches("eggplant", repo.byId("vegetable_eggplant")!!))
        assertTrue(!WordMatching.matches("eggplant", repo.byId("animal_elephant")!!))
        assertEquals("vegetable_eggplant", repo.findSpoken("eggplant", Category.entries)!!.id)
        assertEquals("vegetable_eggplant", repo.findSpoken("brinjal", Category.entries)!!.id)
    }

    @Test
    fun tomatoRecognisesIndianAliases() {
        val tomato = assertNotNull(repo.byId("vegetable_tomato"))
        for (alias in listOf("tomato", "tamatar", "tomoto", "tamato")) {
            assertEquals("vegetable_tomato", repo.findSpoken(alias, Category.entries)!!.id, alias)
            assertTrue(WordMatching.matches(alias, tomato), alias)
        }
    }

    @Test
    fun fuzzyMatchingToleratesMisspellings() {
        val elephant = repo.byId("animal_elephant")!!
        assertTrue(WordMatching.matches("elefant", elephant))
        assertTrue(WordMatching.matches("Elephant", elephant))
    }

    @Test
    fun childStartMode_acceptsValidWordAndAdvancesTurn() {
        val session = GameSession(
            repo = repo,
            round = Round.ROUND_1,
            activeCategories = Round.ROUND_1.categories,
            difficulty = Difficulty.EASY,
            lettieStarts = false,
            random = Random(42),
        )
        session.start()
        val letter = session.requiredLetter
        val candidate = repo.all.first {
            it.category in Round.ROUND_1.categories && it.firstLetter == letter
        }
        val result = session.submitChild(candidate.name)
        assertTrue(result is SubmitResult.Accepted, "Expected Accepted but was $result")
    }

    @Test
    fun wrongLetterIsRejectedStrictly() {
        val session = GameSession(
            repo = repo,
            round = Round.ROUND_1,
            activeCategories = listOf(Category.ANIMAL),
            difficulty = Difficulty.EASY,
            lettieStarts = false,
            random = Random(1),
        )
        session.start()
        val letter = session.requiredLetter
        val wrong = repo.all.first { it.category == Category.ANIMAL && it.firstLetter != letter }
        val result = session.submitChild(wrong.name)
        assertTrue(result is SubmitResult.WrongLetter, "Expected WrongLetter but was $result")
    }

    @Test
    fun noWordIdsCollide() {
        val ids = WordData.all.map { it.id }
        assertEquals(ids.size, ids.toSet().size, "Duplicate word ids found")
    }

    @Test
    fun round1HasEnoughDepthForCommonLetters() {
        val round1 = WordData.all.filter { it.category in Round.ROUND_1.categories }
        // Letters that frequently show up as "last letters" and used to dry out fast.
        for (letter in listOf('E', 'N', 'T', 'R', 'O', 'A', 'S', 'L', 'D')) {
            val count = round1.count { it.firstLetter == letter }
            assertTrue(count >= 4, "Round 1 has only $count words starting with '$letter' (want >= 4)")
        }
    }

    @Test
    fun quizQuestionsHaveExactlyOneCorrectOption() {
        val questions = QuizGenerator.generate(repo, count = 8, random = Random(3))
        assertTrue(questions.isNotEmpty(), "Expected some quiz questions")
        for (q in questions) {
            assertTrue(q.options.any { it.id == q.answerId }, "Answer must be among options")
            val startsWith = q.prompt.contains("starts with")
            val letter = q.prompt.substringAfter('\'').first()
            val matching = q.options.count {
                if (startsWith) it.firstLetter == letter else it.lastLetter == letter
            }
            assertEquals(1, matching, "Question '${q.prompt}' had $matching correct options")
        }
    }

    @Test
    fun lettieAvoidsHandingDeadEndLettersOnMedium() {
        val session = GameSession(
            repo = repo,
            round = Round.ROUND_1,
            activeCategories = Round.ROUND_1.categories,
            difficulty = Difficulty.MEDIUM,
            lettieStarts = false,
            random = Random(7),
        )
        val cats = Round.ROUND_1.categories
        session.start()
        repeat(20) {
            // Child plays any valid word for the current letter.
            val childWord = repo.startingWith(session.requiredLetter, cats, session.playedIds)
                .firstOrNull() ?: return
            session.submitChild(childWord.name)

            // State just before Lettie moves.
            val before = session.playedIds.toSet()
            val lettieCandidates = repo.startingWith(session.requiredLetter, cats, before)
            // Did Lettie even have a move that keeps the game alive?
            val lettieHadSafeOption = lettieCandidates.any { cand ->
                repo.all.any {
                    it.category in cats && it.id != cand.id && it.id !in before &&
                        it.firstLetter.equals(cand.lastLetter, ignoreCase = true)
                }
            }

            val played = session.lettieTurn() ?: return
            val next = played.lastLetter
            val childHasReply = repo.all.any {
                it.category in cats && it.id !in session.playedIds &&
                    it.firstLetter.equals(next, ignoreCase = true)
            }
            // Only hold Lettie accountable when a non-dead-end reply was actually available.
            if (lettieHadSafeOption) {
                assertTrue(childHasReply, "Lettie played '${played.name}' leaving no reply for '$next'")
            }
        }
    }
}
