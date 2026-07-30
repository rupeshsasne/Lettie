# Last Letter Master — Product Requirements Document (PRD)

**Status:** Draft v1.1
**Owner:** Rupesh Sasne
**Last updated:** 2026-07-30
**Platform:** Android (Kotlin Multiplatform + Compose Multiplatform, Material 3)

---

## 1. Overview

### 1.1 One-liner
A friendly, voice-driven "last letter" word game that plays against a young child, helping them practice and win their school word contest — plus a beautiful, illustrated, spoken word encyclopedia to explore and learn from.

### 1.2 Background & motivation
My kid is participating in a **"Last Letter" word contest** at school:
- **Round 1:** Animals, Birds, Fruits, Vegetables, Flowers
- **Round 2:** Cities & Countries

The classic game (a.k.a. *Word Chain* / *Shiritori* / *Antakshari for words*): a player says a word, the next player must say a new word from the category that **starts with the last letter** of the previous word. Repeat until someone can't answer.

This app is a **practice buddy and coach**. It plays the contest *with* the child as a friendly opponent, speaks and listens (voice-first, since the child is an early reader), keeps it fun and encouraging, and doubles as an **illustrated, alphabetized word library** so the child can browse, learn, and hear words in each category before the contest.

### 1.3 Product vision
> "A patient, cheerful game partner that turns contest prep into play — the child talks, the app talks back, everyone learns, and confidence grows."

---

## 2. Target user & context

| Aspect | Detail |
|---|---|
| **Primary user** | A child, **age 5-7** (early reader) |
| **Secondary user** | Parent (setup, supervising, difficulty tuning, adding words) |
| **Reading level** | Minimal reliance on text; **voice + pictures are primary**. Text is supportive/large. |
| **Language** | **English only** (v1) |
| **Usage context** | At home, on a **phone**, usually short 5-15 min sessions, often with a parent nearby |
| **Connectivity** | Internet available for voice; the word library & details must also work **offline** |
| **Device** | Android **phone** (phone-first layouts), `minSdk 24`, portrait-first, touch + voice |

### 2.1 Design implications of a 5-7 year old user
- **Voice-first, read-optional.** Every prompt is spoken aloud; text is a big, friendly caption, never a requirement.
- **Pictures everywhere.** Each word has an illustration/photo; categories have icons/mascots.
- **Forgiving & encouraging.** No harsh "wrong" — gentle hints, retries, celebration.
- **Big tap targets, few choices per screen.** No tiny buttons, no dense lists on game screens.
- **No reading-to-play requirement.** A non-reader should be able to play using only voice + pictures.

---

## 3. Goals & success metrics

### 3.1 Product goals
1. Let the child **practice the contest format** end-to-end via voice against the app.
2. **Build vocabulary & recall** in the 7 contest categories.
3. Make it **fun and repeatable** so the child *wants* to practice.
4. Provide a **browsable, spoken word encyclopedia** for pre-contest learning.

### 3.2 Success metrics (how we'll know it's working)
- **Engagement:** Child completes ≥ 3 game rounds per session; returns ≥ 3 days/week in the 2 weeks before the contest.
- **Learning:** Measurable growth in the number of valid words the child produces per category over time (tracked in-app as a simple "words you knew" count).
- **Voice reliability:** ≥ 90% of clearly-spoken child words are correctly recognized after our normalization pass (see §7.3).
- **Delight (qualitative):** Child chooses to play unprompted; parent reports it's helping.
- **Coverage:** Word library contains a rich, curated set per category (target sizes in §8.2).

### 3.3 Non-goals (v1)
- Multiplayer / online play against other kids.
- Languages other than English.
- iOS/Desktop builds (architecture keeps the door open, but not shipped in v1).
- Accounts, cloud sync, monetization, ads.
- Free-form open-domain word validation (v1 validates only against curated + optional dictionary; see §6.4).

---

## 4. Core concept & game rules

### 4.1 The game loop (App = opponent)
The app is a **friendly opponent named "Lettie"** (a cheerful mascot — see §9.4). Turn-based:

1. A **round** is chosen (Round 1 or Round 2), which fixes the allowed **categories**.
2. The game **picks a starting word** (or lets the child go first — configurable, default: Lettie starts to model an example).
3. On the child's turn:
   - Lettie shows/says the required **starting letter** (the last letter of the previous word) and the active category/categories.
   - The child **speaks a word**. The app listens (mic), transcribes, and validates (§6.4).
   - **Valid** → celebrate, update the chain, it becomes Lettie's turn.
   - **Invalid** → gentle feedback + hint, allow retry (configurable retries, default 2), then optionally skip/help.
4. On Lettie's turn:
   - Lettie **says a valid word** from the category starting with the required letter (chosen by difficulty, §5.4) and **speaks it aloud**, then hands the turn back with the new required letter.
5. The chain continues until a player **can't produce a valid word** (or times out, if timer enabled).
   - If the **child** gets stuck → Lettie offers a hint, then can gracefully end with encouragement ("So close! Want to see some words that would have worked?").
   - If **Lettie** gets "stuck" (deliberately, at easier levels) → child wins, big celebration.

### 4.2 Core rules
- **Last-letter rule:** next word must start with the **last letter** of the previous word.
- **Category rule:** the word must belong to an **allowed category** for the current round.
- **No repeats:** a word already used in the current game cannot be reused (tracked per-game).
- **Valid English word** in the category (see validation §6.4).
- **Strict rules:** the app **enforces real-contest rules** — no "skip the hard letter" mercy rule. This keeps practice true to the actual contest (§4.3).
- **Turn timer (ON):** each turn has a countdown (see §5.4). Running out of time = that player is "stuck." The timer is tuned generously for age 5-7 and used to build gentle excitement, not stress.
- **Letter normalization edge cases** handled explicitly (§4.3).

### 4.3 Rule edge cases & decisions
| Case | Decision (v1) |
|---|---|
| Word ends in a "hard" letter (e.g., X — "Fox") | **Strict enforcement (no skip):** the next word MUST start with that letter, exactly like the real contest. The app curates fallback words for tough letters (Q/X/Z) so **Lettie is never unfairly stuck**; if the **child** can't answer a hard letter, it counts (with encouragement + a "here's what would've worked" teach moment after the game). |
| Plurals / spellings (e.g., "Tomatoes") | Match on normalized singular/known forms; accept common variants. |
| Case & spaces (e.g., "New York") | Case-insensitive; last letter ignores trailing spaces/punctuation. |
| Multi-word names (e.g., "Costa Rica") | Allowed for Cities/Countries; "last letter" = last letter of the full name. |
| Homophones / mis-hearings | Handled via fuzzy matching + confirm chip (§7.3). |
| Q/no-vowel-after letters | Curated fallbacks ensure the app is never unfairly stuck. |

---

## 5. Features

### 5.1 Feature list (priority)
| # | Feature | Priority |
|---|---|---|
| F1 | Voice game vs. Lettie (opponent mode) | **P0 (must)** |
| F2 | Rounds & category selection (Round 1 / Round 2) | **P0** |
| F3 | Word library: alphabetical, categorized browser | **P0** |
| F4 | Word detail card: picture, facts, pronunciation, "speak it" | **P0** |
| F5 | Text-to-speech for all prompts & words | **P0** |
| F6 | Speech recognition for child's spoken words | **P0** |
| F7 | Difficulty levels & parent settings | **P1 (should)** |
| F8 | Hints & "teach me" assist during game | **P1** |
| F9 | Celebrations, sounds, mascot animations | **P1** |
| F10 | Progress: words learned, streaks, simple stats | **P2 (nice)** |
| F11 | Typed-input fallback (when mic unavailable) | **P1** |
| F12 | Parent word editor / add custom words | **P2** |
| F13 | Favorites / "star a word" | **P2** |

### 5.2 F1 — Voice game (opponent mode)
- Full turn-based loop from §4.
- Big **"Tap & Speak" mic button**; visual listening state (waveform/pulse).
- Live spoken + captioned feedback from Lettie.
- Visual **word chain** ("train of words") showing the sequence with pictures.
- **Turn countdown timer** — a friendly shrinking colorful ring with a soft hurry-up chime near the end (see §5.4).
- Round/turn indicators, current letter shown BIG (e.g., a large glowing letter card).
- Pause/quit anytime; resume last game.

### 5.3 F2 — Rounds & categories
- **Round 1:** Animals, Birds, Fruits, Vegetables, Flowers.
- **Round 2:** Cities, Countries.
- Modes:
  - **Single category** (focus practice, e.g., "Animals only").
  - **Round mix** (any word from the round's categories counts — mirrors real contest).
- Category picker uses **big illustrated tiles**, spoken labels on tap.

### 5.4 F7 — Difficulty levels & timer
The **turn timer is ON** to add excitement. Durations are generous for age 5-7 and scale with difficulty. A visible, friendly countdown (e.g., a shrinking colorful ring) with a soft "hurry-up" chime in the last few seconds — celebratory, not scary. Running out of time = "stuck" (strict, like the real contest). Parents can fine-tune the exact seconds in the Parent Zone.

| Level | Lettie's behavior | Turn timer | Retries | Hints |
|---|---|---|---|---|
| **Easy** (default for 5-7) | Uses simple, common words; sometimes "gets stuck" on purpose so the child can win; picks easy next-letters | **~30s** | 3 | Generous, auto-offered |
| **Medium** | Uses moderate words; rarely throws hard letters | **~20s** | 2 | On request |
| **Hard** | Uses uncommon words & tough letters to challenge | **~12s** | 1 | On request |

- The timer **pauses during hints** and while Lettie is speaking (child only "races the clock" on their own thinking time).
- Timer can be turned **off** entirely in the Parent Zone for a no-pressure mode.

### 5.5 F8 — Hints & assist ("Teach me")
- If the child hesitates or is wrong, Lettie offers escalating help:
  1. **Nudge:** "It starts with **T**… it's an animal that says *roar*!" (spoken).
  2. **Picture hint:** show 2-3 candidate pictures; child taps or says one.
  3. **Reveal:** Lettie gives a word and pronounces it, adds it to the child's "words I learned."

### 5.6 F3 — Word library (encyclopedia)
- Entry points: home screen "Explore Words".
- **Category tabs/tiles** (7 categories) + an **"All" view**.
- Within a category: **alphabetical sections (A-Z)** with sticky headers; each item shows **thumbnail + name + small speaker icon** (tap speaker to hear it).
- **Search** (parent-friendly) + **A-Z quick scroll rail**.
- Fully usable **offline**.

### 5.7 F4 — Word detail card
Tapping any item opens a rich, kid-friendly detail screen:
- Large **real photo** (high-quality photographs from day one — see §8).
- **Name** in big text + **"Speak it" 🔊** button (TTS), and a **syllable/pronunciation** hint.
- **Kid-friendly facts** (1-3 short, delightful facts).
- **First letter & last letter** highlighted (reinforces the game skill!) e.g., "**A**pple ends with **E** → next word starts with E".
- **Category badge**.
- For **Cities/Countries**: capital, flag, tiny map, one fun fact (extends the base card).
- Actions: ⭐ favorite, "use in a game", swipe to next word.

### 5.8 F11 — Typed fallback
- If mic permission denied or environment too noisy, show a **large, simple typed input** with word suggestions, so the game is never blocked.

### 5.9 F12 — Parent zone (behind a simple gate)
- Adjust difficulty, timer, retries, voice speed.
- **Add/edit custom words** (e.g., words the child's teacher expects).
- Toggle categories on/off.
- Reset progress.
- Simple **parent gate** (e.g., "hold to enter" or a small math question) to keep kids out of settings.

---

## 6. Data model & content

### 6.1 Categories (v1)
`ANIMAL`, `BIRD`, `FRUIT`, `VEGETABLE`, `FLOWER`, `CITY`, `COUNTRY`.

- **Round 1** = { ANIMAL, BIRD, FRUIT, VEGETABLE, FLOWER }
- **Round 2** = { CITY, COUNTRY }

### 6.2 Word entity (conceptual)
```
Word {
  id: String
  name: String                 // display name, e.g., "Elephant", "New York"
  normalizedName: String       // lowercase, trimmed, for matching
  firstLetter: Char
  lastLetter: Char             // computed from name (ignore spaces/punctuation)
  category: Category
  aliases: [String]            // accepted variants/plurals/mishearings
  pronunciation: String?       // phonetic hint for TTS/child
  syllables: String?           // e.g., "el-e-phant"
  facts: [String]              // 1-3 kid-friendly facts
  imageRef: String             // local drawable/asset id or URL
  difficulty: Easy|Medium|Hard // for opponent word selection
  // Geo-only (City/Country):
  geo: {
    countryOf: String?         // for a city
    capital: String?           // for a country
    flagRef: String?
    mapRef: String?
  }?
}
```

### 6.3 Game state (conceptual)
```
Game {
  round: Round
  activeCategories: Set<Category>
  difficulty: Difficulty
  chain: [ChainEntry{ word, speaker: Child|Lettie }]
  usedWordIds: Set<String>
  requiredLetter: Char
  whoseTurn: Child|Lettie
  retriesLeft: Int
  timer: Optional
  status: Playing|ChildWon|LettieWon|Paused
}
```

### 6.4 Word validation logic
When the child speaks a candidate word, validate in order:
1. **Normalize** transcript (lowercase, trim, strip punctuation; map homophones/aliases §7.3).
2. **Category check:** exists in an active category's word set (curated data is the source of truth in v1).
3. **Last-letter check:** `normalizedName.firstLetter == requiredLetter`.
4. **No-repeat check:** not in `usedWordIds`.
5. **(Optional, P2) dictionary fallback:** for words not in curated set, check an on-device word list for the category to be more permissive; if accepted, add on the fly.

If all pass → valid. Otherwise → specific, gentle feedback ("That's a fruit, but it needs to start with **P**!").

---

## 7. Voice interaction design

### 7.1 Text-to-Speech (TTS) — the app's voice
- All prompts, words, feedback, and facts are **spoken aloud** with a warm, child-friendly voice.
- Android `TextToSpeech` engine; configurable **speech rate** (slower default for age 5-7) and pitch (slightly higher = friendlier).
- Every spoken line is **also captioned** in large text for reading practice (optional to read).
- Words in the library each have a **🔊 button**; detail cards read facts aloud.

### 7.2 Speech recognition (ASR) — hearing the child
- Android `SpeechRecognizer` (online). Requires **RECORD_AUDIO** permission (request with a kid-friendly explainer + parent context).
- Clear **listening UI**: mic pulses, live partial transcript, "I heard: ___" confirmation.
- **Push-to-talk** ("Tap & Speak") default; optional hands-free with silence detection (P2).

### 7.3 Handling child speech (accuracy strategy)
Children mispronounce and ASR struggles with kids' voices, so:
- **Constrained matching:** compare transcript against the **active category word set** using fuzzy matching (e.g., phonetic + edit distance), not open dictionary — dramatically improves accuracy.
- **Alias & homophone maps:** ("lion"/"lyin'", "pair"/"pear", "cheeta"/"cheetah").
- **Confirmation chips:** if confidence is low, show 1-3 **picture chips** of best guesses: "Did you say…?" child taps/speaks to confirm.
- **Retry loop** with encouragement; never a dead end.
- **Typed fallback** (F11) always available.

### 7.4 Voice UX principles
- Never talk over the child; clear turn cues (a soft chime when it's the child's turn).
- Short spoken prompts (kids tune out long speech).
- Always pair audio with a **visual state** (deaf-friendly & noisy-room friendly).

---

## 8. Content requirements

### 8.1 Content quality bar
- **Curated, verified** words per category (no offensive/inappropriate entries).
- Each word has: name, a **real high-quality photograph (from day one)**, **last-letter** correctness verified, difficulty tag, and (ideally) 1-3 **facts** + pronunciation.
- **Photos:** clear, child-appropriate, consistent framing; properly licensed for distribution (see §8.3). Countries also include an accurate **flag**; cities include a recognizable landmark photo.
- **Letter coverage:** for each category, ensure at least a few words for **every starting letter that appears as a "last letter"** in common play, and curated fallbacks for hard letters (Q, X, Z) so Lettie is never unfairly stuck.

### 8.2 Target library sizes (v1)
| Category | Target count |
|---|---|
| Animals | 80-120 |
| Birds | 50-80 |
| Fruits | 40-60 |
| Vegetables | 40-60 |
| Flowers | 40-60 |
| Cities | 100-150 — **India-leaning** (strong coverage of Indian cities) **+ globally famous** cities |
| Countries | ~195 (all recognized countries) |

> **Cities focus:** prioritize well-known **Indian cities** (matches the child's context and likely contest content) alongside globally famous cities, with good letter coverage.
>
> **Photos from day one:** each entry ships with a real photograph. Facts can be phased in (P1) — the game works with names + photos alone — but photos are P0, not placeholders.

### 8.3 Content sourcing & storage
- Ship as **bundled offline data** (e.g., JSON asset in `shared` `commonMain` resources) so the library & game logic work offline.
- **Photos** bundled as compressed assets (e.g., WebP) for offline use; sourced from **properly-licensed** libraries (public-domain / CC0 / licensed stock). Track attribution/licenses where required.
- **App size trade-off:** photos for ~500+ entries add size. Mitigations: aggressive WebP compression + right-sized thumbnails for lists and higher-res only on detail screens; optionally a small on-first-run download of the photo pack with offline caching if bundle size becomes a concern.
- Flags for all countries and landmark photos for cities included.
- Structured to allow parent-added custom words (F12) merged at runtime (parents can pick a device photo for custom words).

---

## 9. UX / UI design

### 9.1 Design language
- **Material 3** (Compose Multiplatform Material3), **child-friendly theme**: rounded shapes, big type, playful but legible font, high contrast, cheerful accent colors per category.
- **Dynamic color** optional; default to a curated bright palette.
- **Large touch targets** (min 56dp), generous spacing, minimal per-screen density.

### 9.2 Key screens
1. **Home / Play hub:** mascot greeting (spoken), two big buttons — **"Play with Lettie"** and **"Explore Words"** — plus small parent/settings gear.
2. **Round & category select:** illustrated tiles; Round 1 / Round 2 toggle.
3. **Game screen:** big current **letter card**, mascot, **word chain train**, giant **Tap & Speak** mic, turn indicator, hint button.
4. **Word library:** category tabs, A-Z sections, quick-scroll rail, search.
5. **Word detail:** hero image, name + 🔊, facts, first/last letter highlight, geo extras.
6. **Celebration/results:** confetti, "You won!"/"Great try!", words-learned recap, replay.
7. **Parent zone:** gated settings + word editor.

### 9.3 Motion & feedback
- Micro-animations on valid words (bounce, sparkle), gentle shake on retry (never scary).
- Sound effects (toggleable): chime (correct), soft boop (retry), fanfare (win).
- Haptics for key actions.

### 9.4 Mascot — "Lettie"
- A cheerful character who is the **opponent + coach**. Speaks, reacts, cheers, and models good sportsmanship. Appears on home, game, and celebration screens with a few expressive states (idle, thinking, happy, cheering, "stuck/oops").

### 9.5 Accessibility
- All interactive elements have content descriptions (TalkBack).
- Captions for all audio; audio for all text.
- Color choices meet contrast guidelines; don't rely on color alone.
- Adjustable speech rate; large-text friendly layouts.

---

## 10. Technical considerations

### 10.1 Architecture
- **Kotlin Multiplatform + Compose Multiplatform**, Material 3 (matches existing repo).
- **Shared module (`commonMain`)**: domain (word model, game engine, validation, word data loading, matching/normalization), presentation (Compose UI, ViewModels via `lifecycle-viewmodel-compose`), and bundled content (JSON + assets).
- **Platform (`androidMain` + `androidApp`)**: `expect/actual` for **TTS** and **SpeechRecognizer**, permissions, and any Android-specific media.
  - Abstractions: `SpeechSynthesizer` (TTS) and `SpeechRecognizer` interfaces in `commonMain`, implemented in `androidMain` — keeps future iOS/Desktop viable.
- **State management:** unidirectional (ViewModel holds `Game` state; Compose observes).

### 10.2 Key technical risks & mitigations
| Risk | Mitigation |
|---|---|
| **ASR accuracy for young kids** | Constrained/fuzzy matching against category sets; confirm chips; typed fallback (§7.3). |
| **Online dependency for voice** | Voice needs net; but library, details, and typed play work offline; degrade gracefully with a friendly "let's type instead" if offline. |
| **Hard letters (Q/X/Z) stalling the game** | Curated fallback words + kid-friendly "skip letter" rule (§4.3). |
| **Content effort (photos/facts)** | Photos are P0 (from day one) — source licensed/CC0 photo sets in batches per category; facts are P1. |
| **App size from photos** | WebP compression, thumbnail vs. full-res split, optional first-run photo-pack download with offline cache (§8.3). |
| **Photo licensing** | Use only public-domain/CC0/licensed images; track attributions. |
| **Mic permission friction** | Kid-safe explainer + parent context; never block the app (fallback to typed). |
| **Latency of TTS/ASR** | Preload TTS; show responsive listening/thinking states so waits feel intentional. |

### 10.3 Privacy & safety (kids)
- **No accounts, no ads, no tracking, no third-party analytics collecting child data** in v1.
- Audio used only transiently for recognition; **not stored/uploaded** by us (beyond what the OS ASR requires).
- All content **curated and child-appropriate**.
- Settings/word-editing behind a **parent gate**.

### 10.4 Non-functional requirements
- **Performance:** smooth 60fps UI; game turn feedback < 300ms after recognition result.
- **Offline:** library + details + typed game fully functional offline.
- **Robustness:** never dead-ends the child; always a path forward (hint/skip/type).
- **Localization-ready:** strings externalized (even though v1 is English-only).

---

## 11. Roadmap / milestones

### Milestone 0 — Foundations
- Project scaffolding in shared/androidApp; theme, navigation, mascot placeholder.
- Word data schema + loader; seed a small word set per category.

### Milestone 1 — Word library (offline) [P0]
- Category browser, A-Z sections, search.
- Word detail card with image + 🔊 TTS + first/last-letter highlight.
- **Value:** child can start learning immediately, even before the game is done.

### Milestone 2 — Voice game core [P0]
- TTS + SpeechRecognizer integration (expect/actual).
- Game engine (turns, chain, validation, no-repeat, required letter).
- Opponent (Lettie) word selection; Easy difficulty; Tap & Speak UI; confirm chips; typed fallback.

### Milestone 3 — Delight & assist [P1]
- Hints/"Teach me", difficulty levels, celebrations, sounds, mascot animations, parent settings.

### Milestone 4 — Content & polish [P1-P2]
- Expand word lists to target sizes; add facts & better images; geo extras for cities/countries.
- Progress/stats, favorites, parent word editor.

---

## 12. Decisions & remaining questions

### 12.1 Resolved decisions
1. **Photos:** ✅ Real **photos from day one** (P0), not placeholders (§8).
2. **Cities:** ✅ Lean toward **Indian cities + globally famous** ones (§8.2).
3. **Contest word list:** ✅ No official list from school — we curate our own coverage.
4. **Timer:** ✅ **Timer ON** to add excitement; generous, age-tuned durations, off-switch in Parent Zone (§5.4).
5. **Rules:** ✅ **Strictly enforce** real-contest rules — no skip-hard-letter mercy rule (§4.3).
6. **Device:** ✅ **Phone-first** (portrait), `minSdk 24` (§2).

### 12.2 Remaining questions (can proceed with defaults)
1. **Exact timer seconds:** the ~30/20/12s defaults OK, or tune? (Parents can adjust anyway.)
2. **Who starts:** default is **Lettie starts** (models an example first). OK, or let the child choose each game?
3. **Photo bundling:** OK to bundle all photos in the app (larger install) vs. a small first-run download? Default: bundle compressed, revisit if size is large.

---

## 13. Appendix

### 13.1 Example gameplay (Round 1, Easy)
```
Lettie: "Let's play! I'll start. My animal is… LION 🦁. Your word must start with N!"
Child (taps mic): "Nightingale"
App: recognizes → BIRD, starts with N, not used → VALID ✨
Lettie: "Yay! Nightingale ends with E. My bird is… EAGLE 🦅. Your turn — start with E!"
Child: "Elephant" → VALID ✨ …chain continues…
Child (stuck on 'T'): taps Hint → "It starts with T… it's a big cat with stripes! 🐯"
Child: "Tiger" → VALID ✨
```

### 13.2 Example word entries
```json
[
  {
    "name": "Elephant", "category": "ANIMAL",
    "firstLetter": "E", "lastLetter": "T",
    "syllables": "el-e-phant", "difficulty": "Easy",
    "facts": ["Elephants are the biggest land animals!",
              "They say hello with their trunks."],
    "aliases": ["elefant"], "imageRef": "img_elephant"
  },
  {
    "name": "Japan", "category": "COUNTRY",
    "firstLetter": "J", "lastLetter": "N",
    "difficulty": "Easy",
    "geo": { "capital": "Tokyo", "flagRef": "flag_jp" },
    "facts": ["Japan has super-fast bullet trains!"],
    "imageRef": "img_japan"
  }
]
```

### 13.3 Category → Round map
| Round | Categories |
|---|---|
| Round 1 | Animals, Birds, Fruits, Vegetables, Flowers |
| Round 2 | Cities, Countries |
