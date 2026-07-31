package com.radix2.llm

import com.radix2.llm.data.LetterCoverage
import com.radix2.llm.data.WordData
import com.radix2.llm.data.WordRepository
import com.radix2.llm.domain.Category
import com.radix2.llm.domain.Difficulty
import com.radix2.llm.domain.KidFacts
import com.radix2.llm.domain.Pronunciation
import com.radix2.llm.domain.Round
import com.radix2.llm.domain.Word
import com.radix2.llm.domain.WordMatching
import com.radix2.llm.game.GameSession
import com.radix2.llm.game.QuizGenerator
import com.radix2.llm.game.SubmitResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
        val brinjal = assertNotNull(repo.byId("vegetable_brinjal"))
        assertTrue(WordMatching.matches("eggplant", brinjal))
        assertTrue(WordMatching.matches("brinjal", brinjal))
        assertTrue(!WordMatching.matches("eggplant", repo.byId("animal_elephant")!!))
        assertEquals("vegetable_brinjal", repo.findSpoken("eggplant", Category.entries)!!.id)
        assertEquals("vegetable_brinjal", repo.findSpoken("brinjal", Category.entries)!!.id)
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
    fun wordBankIsAboutFiveHundred() {
        assertTrue(
            WordData.all.size in 480..540,
            "Expected ~500 words, was ${WordData.all.size}",
        )
    }

    @Test
    fun round1HasUStartersForPracticeAndPlay() {
        val starters = repo.startingWith('U', Round.ROUND_1.categories, emptySet())
        assertTrue(starters.isNotEmpty(), "Round 1 needs U-starters so Practice U stays in-round")
        assertTrue(
            starters.none { it.category == Category.CITY || it.category == Category.COUNTRY },
            "Round 1 U practice must not include cities/countries",
        )
    }

    @Test
    fun lettieAvoidsHandingDeadEndLettersOnHard() {
        val session = GameSession(
            repo = repo,
            round = Round.ROUND_1,
            activeCategories = Round.ROUND_1.categories,
            difficulty = Difficulty.HARD,
            lettieStarts = false,
        )
        val cats = Round.ROUND_1.categories
        session.start()
        repeat(20) {
            val childWord = repo.startingWith(session.requiredLetter, cats, session.playedIds)
                .firstOrNull() ?: return
            session.submitChild(childWord.name)

            val before = session.playedIds.toSet()
            val lettieCandidates = repo.startingWith(session.requiredLetter, cats, before)
            val lettieHadSafeOption = lettieCandidates.any { cand ->
                repo.all.any {
                    it.category in cats && it.id != cand.id && it.id !in before &&
                        it.firstLetter.equals(cand.lastLetter, ignoreCase = true)
                }
            }
            val played = session.lettieTurn() ?: return
            if (!lettieHadSafeOption) return@repeat
            val childCanReply = repo.startingWith(played.lastLetter, cats, session.playedIds).isNotEmpty()
            assertTrue(
                childCanReply,
                "Hard Lettie played ${played.name} ending '${played.lastLetter}' with 0 Round 1 replies",
            )
        }
    }

    @Test
    fun topEndingLettersHaveEnoughStartersPerRound() {
        for (round in Round.entries) {
            val words = LetterCoverage.wordsInRound(round)
            for (letter in LetterCoverage.topEndings(round)) {
                val starters = LetterCoverage.starterCount(letter, words)
                assertTrue(
                    starters >= LetterCoverage.minTopStarters,
                    "$round: only $starters words start with '$letter' (want >= ${LetterCoverage.minTopStarters})",
                )
            }
        }
    }

    @Test
    fun easyWordsDoNotEndOnNearDeadLetters() {
        for (round in Round.entries) {
            val words = LetterCoverage.wordsInRound(round)
            for (word in words.filter { it.difficulty == Difficulty.EASY }) {
                val starters = LetterCoverage.starterCount(word.lastLetter, words)
                assertTrue(
                    starters >= LetterCoverage.minEasyExitStarters,
                    "$round Easy '${word.name}' ends with '${word.lastLetter}' but only $starters starters",
                )
            }
        }
    }

    @Test
    fun quizQuestionsHaveExactlyOneCorrectOption() {
        val questions = QuizGenerator.generate(repo, count = 8)
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
        )
        val cats = Round.ROUND_1.categories
        session.start()
        repeat(20) {
            val childWord = repo.startingWith(session.requiredLetter, cats, session.playedIds)
                .firstOrNull() ?: return
            session.submitChild(childWord.name)

            val before = session.playedIds.toSet()
            val lettieCandidates = repo.startingWith(session.requiredLetter, cats, before)
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
            if (lettieHadSafeOption) {
                assertTrue(childHasReply, "Lettie played '${played.name}' leaving no reply for '$next'")
            }
        }
    }

    @Test
    fun hardLettieTargetsChildWeakEndingLetters() {
        val cats = listOf(Category.ANIMAL)
        // Force equal branching so letter strength decides: plant a fake weakness on one letter.
        val strengths = mutableMapOf<Char, Int>()
        // Make every letter strong except the one that appears most as an Easy animal ending.
        val animalEndings = repo.all
            .filter { it.category == Category.ANIMAL }
            .groupingBy { it.lastLetter.uppercaseChar() }
            .eachCount()
        val weakLetter = animalEndings.maxByOrNull { it.value }!!.key
        ('A'..'Z').forEach { strengths[it] = 10 }
        strengths[weakLetter] = -10

        val session = GameSession(
            repo = repo,
            round = Round.ROUND_1,
            activeCategories = cats,
            difficulty = Difficulty.HARD,
            lettieStarts = false,
            childLetterStrength = { strengths[it.uppercaseChar()] ?: 0 },
        )
        session.start()
        // Give Lettie a turn from a letter with several animal options.
        val starter = repo.startingWith(session.requiredLetter, cats, emptySet())
            .firstOrNull { cand ->
                repo.startingWith(cand.lastLetter, cats, setOf(cand.id)).size >= 2
            } ?: repo.startingWith(session.requiredLetter, cats, emptySet()).first()
        session.submitChild(starter.name)

        val before = session.playedIds.toSet()
        val candidates = repo.startingWith(session.requiredLetter, cats, before)
        val minBranch = candidates.minOf { cand ->
            repo.all.count {
                it.category in cats && it.id != cand.id && it.id !in before &&
                    it.firstLetter.equals(cand.lastLetter, ignoreCase = true)
            }
        }
        val tightest = candidates.filter { cand ->
            repo.all.count {
                it.category in cats && it.id != cand.id && it.id !in before &&
                    it.firstLetter.equals(cand.lastLetter, ignoreCase = true)
            } == minBranch
        }
        // Among equally tight moves, prefer ending on the weak letter when available.
        val weakEnds = tightest.filter { it.lastLetter.equals(weakLetter, ignoreCase = true) }
        if (weakEnds.isEmpty()) return // no comparable choice this turn — skip

        val played = assertNotNull(session.lettieTurn())
        assertEquals(
            weakLetter,
            played.lastLetter.uppercaseChar(),
            "Expected Hard Lettie to end on weak '$weakLetter', played ${played.name}",
        )
    }

    @Test
    fun lettieOpenerVariesAmongStrongStarters() {
        fun opener(): Word {
            val session = GameSession(
                repo = repo,
                round = Round.ROUND_1,
                activeCategories = Round.ROUND_1.categories,
                difficulty = Difficulty.EASY,
                lettieStarts = true,
            )
            return assertNotNull(session.start())
        }
        val openers = (1..40).map { opener() }
        assertTrue(openers.all { it.difficulty == Difficulty.EASY })
        assertTrue(
            openers.map { it.id }.toSet().size >= 3,
            "Expected varied openers, got ${openers.map { it.name }.distinct()}",
        )
    }

    @Test
    fun pronunciationFallsBackWhenSyllablesMissing() {
        val cat = assertNotNull(repo.byId("animal_cat"))
        assertTrue(cat.syllables == null)
        val said = Pronunciation.sayItLike(cat)
        assertTrue(said.isNotBlank())
        assertTrue(said.contains("c", ignoreCase = true))
    }

    @Test
    fun kidFactsAreAlwaysTwoLines() {
        val word = assertNotNull(repo.byId("animal_fox"))
        val facts = KidFacts.forWord(word)
        assertEquals(2, facts.size)
        assertTrue(facts[0].isNotBlank())
        assertTrue(facts[1].isNotBlank())
        assertFalse(facts[0].contains("is an animal", ignoreCase = true))
        assertFalse(facts[1].contains("next word must start", ignoreCase = true))
    }

    @Test
    fun kidFactsCoverEveryWordWithUniqueTrivia() {
        val all = repo.all
        assertTrue(all.size >= 400)
        val seen = mutableSetOf<String>()
        for (word in all) {
            val facts = KidFacts.forWord(word)
            assertEquals(2, facts.size, word.id)
            assertTrue(facts[0].isNotBlank(), word.id)
            assertTrue(facts[1].isNotBlank(), word.id)
            assertFalse(facts.any { it.contains("is an animal. You might see", ignoreCase = true) }, word.id)
            assertFalse(facts.any { it.contains("Cities are busy places", ignoreCase = true) }, word.id)
            assertFalse(facts.any { it.contains("Countries are big places", ignoreCase = true) }, word.id)
            assertFalse(facts.any { it.contains("next word must start with", ignoreCase = true) }, word.id)
            val key = facts.joinToString("|")
            assertTrue(seen.add(key), "duplicate facts for ${word.id}")
        }
    }
}
