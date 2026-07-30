package com.radix2.llm.domain

/**
 * Two unique kid-friendly fun facts per word (keyed by [Word.id]).
 * Used by the detail "About" card — never generic category blurbs.
 */
object FunFacts {
    fun forId(id: String): List<String>? = facts[id]

    private val facts: Map<String, List<String>> = mapOf(
        "animal_alligator" to listOf(
            "Alligators grow new teeth again and again if one falls out!",
            "Baby alligators hitch rides on Mom's head for safety.",
        ),
        "animal_ant" to listOf(
            "Ants can lift things 10 to 50 times their own weight!",
            "Some ant colonies have millions of ants working as a team.",
        ),
        "animal_antelope" to listOf(
            "Many antelopes spring high in the air when they run — called stotting.",
            "They live in herds so many eyes can watch for danger.",
        ),
        "animal_axolotl" to listOf(
            "Axolotls can regrow arms, legs, and even heart bits!",
            "They stay looking like cute water babies forever.",
        ),
        "animal_bat" to listOf(
            "Bats use echoes to \"see\" in the dark — echolocation!",
            "Most bats gobble insects and help farmers.",
        ),
        "animal_bear" to listOf(
            "Bears have a nose better than a bloodhound's!",
            "Many bears nap through cold winters in cozy dens.",
        ),
        "animal_bison" to listOf(
            "Bison are the biggest land animals in North America.",
            "Thick fur keeps them cozy in blizzards.",
        ),
        "animal_bobcat" to listOf(
            "Bobcats got their name from their short \"bobbed\" tails!",
            "They leap high to snatch birds.",
        ),
        "animal_buffalo" to listOf(
            "Buffalo love muddy wallows to cool their thick coats.",
            "They live in big herds on wide grasslands.",
        ),
        "animal_camel" to listOf(
            "Camels store fat in their humps — not water bottles!",
            "They can go days without a drink in the desert.",
        ),
        "animal_cat" to listOf(
            "Cats purr when they feel happy and safe.",
            "A cat's whiskers help it measure if a gap is too tight.",
        ),
        "animal_chameleon" to listOf(
            "Chameleons peek with each eye in a different direction!",
            "Their sticky tongues zip out to catch bugs.",
        ),
        "animal_cheetah" to listOf(
            "Cheetahs are the fastest land animals — about 100 km/h!",
            "Their long tails steer like a rudder when they sprint.",
        ),
        "animal_chipmunk" to listOf(
            "Chipmunks stuff cheeks with nuts like shopping bags!",
            "They bury snacks for winter.",
        ),
        "animal_cougar" to listOf(
            "Cougars are also called mountain lions or pumas.",
            "They can jump farther than a school bus is long!",
        ),
        "animal_cow" to listOf(
            "Cows have four stomach parts to digest tough grass!",
            "Cow friends often hang out in the same little group.",
        ),
        "animal_crab" to listOf(
            "Crabs often walk sideways and wear hard shell armor.",
            "Some crabs borrow empty shells as mobile homes.",
        ),
        "animal_crocodile" to listOf(
            "Crocodiles hold their breath underwater for a long time.",
            "Moms carefully carry babies in their big mouths — gently!",
        ),
        "animal_deer" to listOf(
            "Deer grow brand-new antlers every year.",
            "Baby deer (fawns) have spots that help them hide in grass.",
        ),
        "animal_dingo" to listOf(
            "Dingoes are wild dogs of Australia.",
            "They howl more than they bark.",
        ),
        "animal_dog" to listOf(
            "A dog's nose print is as unique as a human fingerprint!",
            "Dogs can smell feelings and find people by scent.",
        ),
        "animal_dolphin" to listOf(
            "Dolphins chat with clicks and whistles underwater.",
            "They love to play and ride waves beside boats.",
        ),
        "animal_donkey" to listOf(
            "Donkeys' huge ears catch tiny sounds from far away.",
            "They remember paths and friends for years.",
        ),
        "animal_earthworm" to listOf(
            "Earthworms make soil soft and rich for plants.",
            "Some can regrow parts if carefully broken.",
        ),
        "animal_eel" to listOf(
            "Electric eels can zap prey with a real shock!",
            "Some eels travel thousands of kilometers to lay eggs.",
        ),
        "animal_elephant" to listOf(
            "Elephants never forget their friends.",
            "Baby elephants suck their trunks like thumbs!",
        ),
        "animal_elk" to listOf(
            "Elk make a loud bugling call that echoes in mountains.",
            "Males grow huge new antlers every year.",
        ),
        "animal_ermine" to listOf(
            "Ermines turn white in winter to hide in snow.",
            "They're tiny hunters packed with energy.",
        ),
        "animal_ferret" to listOf(
            "Ferrets squeeze into tunnels like furry noodles.",
            "They do a happy \"weasel war dance\" when excited!",
        ),
        "animal_fox" to listOf(
            "Foxes use their bushy tails as warm winter blankets.",
            "They can hear a mouse squeak under the snow.",
        ),
        "animal_frog" to listOf(
            "Frogs drink water right through their skin!",
            "Some frogs freeze solid in winter and thaw out in spring.",
        ),
        "animal_gazelle" to listOf(
            "Gazelles spring high when they run — called stotting.",
            "They're some of the fastest antelopes.",
        ),
        "animal_gibbon" to listOf(
            "Gibbons swing arm-over-arm through the trees.",
            "Partners sing loud morning duets together.",
        ),
        "animal_giraffe" to listOf(
            "Giraffes have purple tongues almost as long as your arm!",
            "A baby giraffe can stand within about an hour of birth.",
        ),
        "animal_goat" to listOf(
            "Goats have rectangular pupils that help them see sideways.",
            "They climb steep rocks like little mountain athletes.",
        ),
        "animal_gorilla" to listOf(
            "Gorillas build soft leafy nest beds every night.",
            "They talk with chest beats and gentle grunts.",
        ),
        "animal_hamster" to listOf(
            "Hamsters stuff their cheeks like tiny grocery bags!",
            "They're night owls and love running on wheels after dark.",
        ),
        "animal_hedgehog" to listOf(
            "Hedgehogs roll into a prickly ball when scared.",
            "They crunch insects for dinner.",
        ),
        "animal_hippo" to listOf(
            "Hippos make their own sunscreen goo!",
            "They cool off in water all day but aren't great swimmers.",
        ),
        "animal_horse" to listOf(
            "Horses can sleep standing up thanks to special leg locks.",
            "They talk with ear wiggles, snorts, and neighs.",
        ),
        "animal_iguana" to listOf(
            "Iguanas love sunny rocks for warming up like living solar panels.",
            "They can drop their tail to escape — then grow a new one!",
        ),
        "animal_jackal" to listOf(
            "Jackals often mate for life and raise pups as a team.",
            "Their night howls sound almost like laughter.",
        ),
        "animal_jaguar" to listOf(
            "Jaguars have the strongest bite of any big cat.",
            "Each jaguar's rose-shaped spots are one of a kind.",
        ),
        "animal_kangaroo" to listOf(
            "Kangaroos can't easily walk backward!",
            "Baby joeys grow cozy in Mom's pouch.",
        ),
        "animal_koala" to listOf(
            "Koalas sleep up to 20 hours after munching eucalyptus.",
            "Their fingerprints look surprisingly like ours!",
        ),
        "animal_lamb" to listOf(
            "Lambs know Mom's voice and bleat right back.",
            "Their soft wool grows into yarn and sweaters.",
        ),
        "animal_lemur" to listOf(
            "Lemurs live only on the island of Madagascar.",
            "Ring-tailed lemurs sunbathe with arms open wide.",
        ),
        "animal_leopard" to listOf(
            "Leopards haul dinner up trees to keep it safe.",
            "Spots help them hide in dappled forest light.",
        ),
        "animal_lion" to listOf(
            "Lionesses do most of the hunting for the pride.",
            "A lion's roar can travel across the whole savanna.",
        ),
        "animal_lizard" to listOf(
            "Many lizards grow a new tail if they lose one.",
            "They soak up sun on warm rocks to get energy.",
        ),
        "animal_llama" to listOf(
            "Llamas spit when annoyed — usually at other llamas!",
            "People use them as pack animals on mountain trails.",
        ),
        "animal_meerkat" to listOf(
            "Meerkats take turns as lookouts while others dig.",
            "They teach pups how to handle scorpions safely!",
        ),
        "animal_monkey" to listOf(
            "Many monkeys use their tail like an extra hand in the trees.",
            "They make lots of faces and calls to chat with friends.",
        ),
        "animal_moose" to listOf(
            "Moose are the tallest deer in the world.",
            "Male antlers look like giant flat hands.",
        ),
        "animal_mouse" to listOf(
            "Mice squeeze through holes as small as a pencil!",
            "They sing tiny songs almost too high for us to hear.",
        ),
        "animal_narwhal" to listOf(
            "A narwhal's long tusk is actually a giant tooth!",
            "People call them unicorns of the sea.",
        ),
        "animal_newt" to listOf(
            "Newts can regrow lost arms and legs!",
            "They live both in water and on land at different times.",
        ),
        "animal_numbat" to listOf(
            "Numbats eat thousands of termites every day.",
            "They're striped marsupials from Australia.",
        ),
        "animal_octopus" to listOf(
            "Octopuses have three hearts and blue blood!",
            "They squeeze through tiny gaps and change color in a blink.",
        ),
        "animal_opossum" to listOf(
            "Opossums \"play dead\" when scared — it's a real reflex!",
            "They gobble ticks and help gardens.",
        ),
        "animal_otter" to listOf(
            "Sea otters hold hands while sleeping so they don't drift apart!",
            "They smash shellfish open with rock tools.",
        ),
        "animal_ox" to listOf(
            "Oxen are strong cattle trained to pull heavy loads.",
            "People have teamed with oxen for thousands of years.",
        ),
        "animal_panda" to listOf(
            "Giant pandas eat almost nothing but bamboo all day.",
            "Newborn pandas are tinier than a soda can!",
        ),
        "animal_pangolin" to listOf(
            "Pangolins are the only mammals covered in big scales.",
            "They curl into an armored ball when scared.",
        ),
        "animal_pig" to listOf(
            "Pigs are clever problem-solvers and love cooling mud baths.",
            "Their sniffers are excellent at finding buried snacks.",
        ),
        "animal_porcupine" to listOf(
            "Porcupine quills are sharp hairs — not thrown like darts!",
            "They raise their quills to look bigger when scared.",
        ),
        "animal_puma" to listOf(
            "Pumas are powerful cats that can leap huge distances.",
            "They live from Canada all the way to South America.",
        ),
        "animal_quokka" to listOf(
            "Quokkas look like they're always smiling for photos!",
            "They mostly live on islands near Australia.",
        ),
        "animal_rabbit" to listOf(
            "Rabbit teeth never stop growing — chewing keeps them short!",
            "They thump their back feet to warn of danger.",
        ),
        "animal_raccoon" to listOf(
            "Raccoon paws work almost like little hands on jars!",
            "They dunk food in water when they get the chance.",
        ),
        "animal_rat" to listOf(
            "Rats are clever and even care for injured friends.",
            "Lab rats have helped scientists learn about brains.",
        ),
        "animal_rhino" to listOf(
            "A rhino's horn is made of keratin — like your fingernails!",
            "They love mud baths to cool and protect their skin.",
        ),
        "animal_seal" to listOf(
            "Seals clap and bark — some keep a beat!",
            "Thick blubber keeps them toasty in icy seas.",
        ),
        "animal_sheep" to listOf(
            "Sheep remember faces of other sheep and friendly people.",
            "One sheep can grow enough wool for a whole sweater.",
        ),
        "animal_sloth" to listOf(
            "Sloths move so slowly that algae can grow on their fur!",
            "They hang upside-down and nap high in trees.",
        ),
        "animal_snake" to listOf(
            "Snakes smell the world with their tongues!",
            "They shed their whole skin as they grow.",
        ),
        "animal_squirrel" to listOf(
            "Squirrels fake-bury nuts to trick thieves!",
            "They twist their ankles to climb down trees headfirst.",
        ),
        "animal_tamarin" to listOf(
            "Tiny tamarin monkeys have wild mustache-like fur!",
            "They leap through South American treetops in family groups.",
        ),
        "animal_tapir" to listOf(
            "Tapirs look like a pig–elephant mix with a short trunk.",
            "Babies wear stripes and spots for camouflage.",
        ),
        "animal_tiger" to listOf(
            "Every tiger's stripe pattern is unique — like a fingerprint!",
            "Tigers love water and are strong swimmers.",
        ),
        "animal_tortoise" to listOf(
            "Tortoises can live longer than many humans!",
            "They're land turtles with sturdy dome shells.",
        ),
        "animal_turtle" to listOf(
            "A turtle's shell is part of its body — it can't leave home!",
            "Some sea turtles navigate using Earth's magnetic field.",
        ),
        "animal_wallaby" to listOf(
            "Wallabies are like smaller kangaroo cousins.",
            "Babies grow in a warm pouch too.",
        ),
        "animal_walrus" to listOf(
            "Walruses use long tusks to haul onto ice.",
            "They can doze while floating with special air sacs.",
        ),
        "animal_weasel" to listOf(
            "Weasels are skinny enough to chase mice into holes.",
            "They zip around with boundless energy.",
        ),
        "animal_whale" to listOf(
            "Blue whales are the biggest animals that ever lived!",
            "Whales sing underwater songs that travel for miles.",
        ),
        "animal_wolf" to listOf(
            "Wolves howl to keep the pack together over long distances.",
            "They are fantastic team hunters.",
        ),
        "animal_wombat" to listOf(
            "Wombat poop is cube-shaped — nature's dice!",
            "Their burrows are super strong.",
        ),
        "animal_yak" to listOf(
            "Yaks wear thick coats built for freezing mountain life.",
            "People use yak milk, wool, and strength in the Himalayas.",
        ),
        "animal_yellowfin" to listOf(
            "Yellowfin tuna are speedy ocean swimmers with yellow fins.",
            "They travel in schools across warm seas.",
        ),
        "animal_yorkshireterrier" to listOf(
            "Yorkies were brave little rat hunters in old mills.",
            "Their long silky hair needs gentle brushing.",
        ),
        "animal_zebra" to listOf(
            "Every zebra's stripe pattern is unique!",
            "Stripes may confuse biting flies and help the herd blend.",
        ),
        "bird_albatross" to listOf(
            "Albatrosses glide for hours without flapping.",
            "Some fly around the whole world over oceans!",
        ),
        "bird_canary" to listOf(
            "Canaries were once taken into mines to warn of bad air.",
            "They sing bright cheerful songs.",
        ),
        "bird_cardinal" to listOf(
            "Male cardinals are bright red like living stop signs.",
            "They don't migrate — they brighten winter gardens.",
        ),
        "bird_crow" to listOf(
            "Crows are puzzle geniuses and remember human faces.",
            "They sometimes leave shiny gifts for people they like.",
        ),
        "bird_cuckoo" to listOf(
            "Some cuckoos lay eggs in other birds' nests!",
            "Their call sounds like their name: cu-ckoo.",
        ),
        "bird_dove" to listOf(
            "Doves coo softly to their partners.",
            "They're gentle birds often seen as symbols of peace.",
        ),
        "bird_duck" to listOf(
            "Duck feet work like paddles for swimming.",
            "They waterproof feathers with special oil.",
        ),
        "bird_eagle" to listOf(
            "Eagles spot prey from way up in the sky.",
            "Some nests get as big as a small car!",
        ),
        "bird_egret" to listOf(
            "Egrets stand still, then strike fast for fish.",
            "They're elegant white wading birds.",
        ),
        "bird_eider" to listOf(
            "Eider ducks grow super-soft down feathers.",
            "People once used eiderdown for the warmest quilts.",
        ),
        "bird_emperorpenguin" to listOf(
            "Emperor penguins huddle in huge circles to share heat.",
            "Dads balance eggs on their feet through icy winters.",
        ),
        "bird_emu" to listOf(
            "Emus are giant birds that run fast but can't fly.",
            "Dad sits on the eggs until they hatch.",
        ),
        "bird_falcon" to listOf(
            "Peregrine falcons dive faster than race cars!",
            "They're the fastest animals on Earth.",
        ),
        "bird_finch" to listOf(
            "Finches crack seeds with strong little beaks.",
            "Darwin studied finches to learn how animals change.",
        ),
        "bird_flamingo" to listOf(
            "Flamingos turn pink from the shrimp and algae they eat!",
            "They often rest on one leg.",
        ),
        "bird_goose" to listOf(
            "Geese fly in a V shape to save energy.",
            "They honk to keep the whole flock together.",
        ),
        "bird_hawk" to listOf(
            "Hawks ride warm air currents without much flapping.",
            "Their eyesight is many times sharper than ours.",
        ),
        "bird_hen" to listOf(
            "Hens cluck to call chicks when they find tasty food.",
            "An egg forms inside a hen in about a day.",
        ),
        "bird_heron" to listOf(
            "Herons spear fish with dagger beaks.",
            "They fold long necks into an S when flying.",
        ),
        "bird_hornbill" to listOf(
            "Hornbills have a helmet-like casque on their beaks.",
            "Moms seal into tree nests while chicks grow.",
        ),
        "bird_ibis" to listOf(
            "Ibises dig snacks from mud with curved beaks.",
            "Sacred ibises were special in ancient Egypt.",
        ),
        "bird_kingfisher" to listOf(
            "Kingfishers dive headfirst to catch fish.",
            "Their feathers look like flying jewels.",
        ),
        "bird_kite" to listOf(
            "Kite birds soar and tilt like living paper kites.",
            "Some snatch food right out of the air.",
        ),
        "bird_lark" to listOf(
            "Larks sing while flying high in the sky.",
            "Their songs sound like sparkling music.",
        ),
        "bird_magpie" to listOf(
            "Magpies recognize themselves in mirrors!",
            "They're noisy, clever members of the crow family.",
        ),
        "bird_myna" to listOf(
            "Mynas are expert talkers and copy city sounds.",
            "They strut around with bold yellow beak patches.",
        ),
        "bird_nightingale" to listOf(
            "Nightingales sing beautiful songs even at night.",
            "Poets have written about their music for centuries.",
        ),
        "bird_noddy" to listOf(
            "Noddy terns nest on tropical islands.",
            "They're friendly seabirds that sometimes land on boats.",
        ),
        "bird_nuthatch" to listOf(
            "Nuthatches walk headfirst down tree trunks!",
            "They jam nuts into bark and hammer them open.",
        ),
        "bird_oriole" to listOf(
            "Orioles weave hanging pouch nests like little baskets.",
            "Many glow orange like Halloween candy.",
        ),
        "bird_ostrich" to listOf(
            "Ostriches are the biggest birds and lay giant eggs.",
            "They outrun most horses over short distances.",
        ),
        "bird_owl" to listOf(
            "Owls twist their heads almost all the way around.",
            "Soft feathers make their flight whisper-quiet.",
        ),
        "bird_parrot" to listOf(
            "Parrots copy human words and funny sounds.",
            "They climb with their beak like a third foot.",
        ),
        "bird_peacock" to listOf(
            "Peacocks fan huge colorful tails to impress friends.",
            "Those \"eyes\" on the feathers aren't real eyes!",
        ),
        "bird_penguin" to listOf(
            "Penguins \"fly\" underwater with flipper-wings.",
            "Some dads keep eggs warm on their feet.",
        ),
        "bird_pigeon" to listOf(
            "Pigeons find their way home from far away.",
            "They were once used to carry messages.",
        ),
        "bird_quail" to listOf(
            "Quail chicks walk and find food soon after hatching.",
            "They whistle a funny \"bob-white\" call.",
        ),
        "bird_raven" to listOf(
            "Ravens solve puzzles and plan ahead.",
            "They even slide on snow for fun!",
        ),
        "bird_robin" to listOf(
            "Robins tug earthworms from spring lawns.",
            "Bright orange chests make them easy to spot.",
        ),
        "bird_rooster" to listOf(
            "Roosters crow to claim their yard — even before sunrise!",
            "They fluff up to look bigger protecting hens.",
        ),
        "bird_seagull" to listOf(
            "Seagulls drink both fresh and salt water.",
            "They're clever at snatching picnic fries!",
        ),
        "bird_sparrow" to listOf(
            "Sparrows chirp cheerful city songs.",
            "They take dust baths to clean their feathers.",
        ),
        "bird_starling" to listOf(
            "Huge starling flocks swirl in sky dances called murmurations.",
            "They mimic car alarms and other birds.",
        ),
        "bird_stork" to listOf(
            "Storks nest on rooftops in some countries.",
            "They clap beaks instead of singing pretty songs.",
        ),
        "bird_swan" to listOf(
            "Swans often swim in graceful lifelong pairs.",
            "Baby swans are called cygnets.",
        ),
        "bird_swift" to listOf(
            "Swifts spend most of their life flying — even sleeping on the wing!",
            "They catch insects high in the sky.",
        ),
        "bird_tern" to listOf(
            "Arctic terns migrate farther than almost any animal!",
            "They dive-bomb fish with sharp beaks.",
        ),
        "bird_toucan" to listOf(
            "A toucan's giant beak is surprisingly light.",
            "They toss fruit up and catch it.",
        ),
        "bird_turkey" to listOf(
            "Wild turkeys can fly short distances!",
            "They gobble and puff up for fancy shows.",
        ),
        "bird_vulture" to listOf(
            "Vultures clean nature by eating leftovers — recyclers!",
            "They soar for hours searching for food.",
        ),
        "bird_woodpecker" to listOf(
            "Woodpeckers drum on trees to find bugs and say hello.",
            "Special skull padding protects their brains.",
        ),
        "bird_wren" to listOf(
            "Tiny wrens sing surprisingly loud songs.",
            "They stuff nests into tiny nooks and crannies.",
        ),
        "bird_yellowhammer" to listOf(
            "Yellowhammers are bright yellow songbirds of fields.",
            "Their song sounds a bit like \"a little bit of bread and no cheese!\"",
        ),
        "city_addisababa" to listOf(
            "Addis Ababa means \"new flower\" and sits high in the mountains.",
            "It's home to African Union headquarters.",
        ),
        "city_agra" to listOf(
            "Agra is home to the Taj Mahal, a sparkling white marble wonder.",
            "Craftspeople carved flowers into stone so fine they look real.",
        ),
        "city_ahmedabad" to listOf(
            "Ahmedabad's old city has colorful pols — tiny neighborhood lanes.",
            "It's famous for fluffy dhokla snacks.",
        ),
        "city_aleppo" to listOf(
            "Aleppo's ancient souk was one of the world's great markets.",
            "The citadel hill has watched the city for millennia.",
        ),
        "city_alexandria" to listOf(
            "Ancient Alexandria guarded a famous lighthouse wonder.",
            "Today's Corniche promenade hugs the Mediterranean.",
        ),
        "city_amritsar" to listOf(
            "Amritsar's Golden Temple glows in a sacred pool.",
            "Warm vegetarian langar meals welcome everyone.",
        ),
        "city_amsterdam" to listOf(
            "Amsterdam has more bikes than people!",
            "Houses lean along canals with hook-and-pulley lofts.",
        ),
        "city_ankara" to listOf(
            "Ankara is Turkey's capital, high on the Anatolian plateau.",
            "A hilltop citadel overlooks modern boulevards.",
        ),
        "city_astana" to listOf(
            "Astana (now Nur-Sultan / Astana again) has futuristic white tents and towers.",
            "Winters on the steppe can be fiercely cold.",
        ),
        "city_athens" to listOf(
            "Athens is home to the Parthenon on the Acropolis hill.",
            "Democracy ideas were born here thousands of years ago.",
        ),
        "city_auckland" to listOf(
            "Auckland is built between two harbors among volcanic cones.",
            "Sailboats fill the bays — it's a City of Sails.",
        ),
        "city_bangkok" to listOf(
            "Bangkok's temples glitter with gold and glass mosaics.",
            "Long-tail boats zip along the Chao Phraya River.",
        ),
        "city_bengaluru" to listOf(
            "Bengaluru is nicknamed India's Garden City and tech hub.",
            "Pleasant weather earned it another nickname: Air-Conditioned City.",
        ),
        "city_berlin" to listOf(
            "Berlin's Brandenburg Gate is a symbol of reunited Germany.",
            "Street art covers whole walls with huge murals.",
        ),
        "city_bhopal" to listOf(
            "Bhopal is called the City of Lakes.",
            "Upper and Lower Lakes sit right beside the city.",
        ),
        "city_cairo" to listOf(
            "Cairo guards the Giza pyramids just outside the city.",
            "The Nile River has fed this region for thousands of years.",
        ),
        "city_chennai" to listOf(
            "Chennai sits on the Bay of Bengal with long sandy beaches.",
            "It's a capital of South Indian classical music and dance.",
        ),
        "city_dallas" to listOf(
            "Dallas shines with modern skyline and big Texas pride.",
            "A famous grassy park remembers a turning point in U.S. history.",
        ),
        "city_delhi" to listOf(
            "Delhi has been a capital for empires for centuries.",
            "You can find ancient forts and busy modern metro trains together.",
        ),
        "city_denver" to listOf(
            "Denver is the Mile High City — about 1,600 meters up!",
            "Rocky Mountain views sit right on the horizon.",
        ),
        "city_dhaka" to listOf(
            "Dhaka is one of the world's busiest capitals.",
            "Colorful rickshaws weave through the streets.",
        ),
        "city_doha" to listOf(
            "Doha's skyline pops out of the desert by the Persian Gulf.",
            "A giant park of sculptures and museums faces the water.",
        ),
        "city_dresden" to listOf(
            "Dresden rebuilt its fairy-tale historic center stone by stone.",
            "The Elbe River mirrors baroque palaces.",
        ),
        "city_dubai" to listOf(
            "Dubai built the Burj Khalifa — the world's tallest building!",
            "A busy creek still carries wooden dhow boats.",
        ),
        "city_durban" to listOf(
            "Durban's warm Indian Ocean beaches invite surfers.",
            "Bunny chow — curry in a bread loaf — was invented here.",
        ),
        "city_edinburgh" to listOf(
            "Edinburgh Castle sits on an extinct volcano rock!",
            "Every August the city bursts with festival performers.",
        ),
        "city_edmonton" to listOf(
            "Edmonton hosts one of Canada's biggest shopping malls — and huge parks.",
            "Northern lights sometimes dance above the city.",
        ),
        "city_erbil" to listOf(
            "Erbil's hilltop citadel may be one of the oldest continually lived-in places.",
            "Bazaars buzz at the foot of the ancient mound.",
        ),
        "city_essen" to listOf(
            "Essen grew from coal and steel into a green culture city.",
            "An old coal mine here is a UNESCO site.",
        ),
        "city_eugene" to listOf(
            "Eugene, Oregon, loves running, bikes, and huge trees.",
            "It's nicknamed Track Town USA.",
        ),
        "city_hyderabad" to listOf(
            "Hyderabad is famous for fragrant biryani rice.",
            "The Charminar's four towers watch over the old bazaars.",
        ),
        "city_indore" to listOf(
            "Indore often wins awards as one of India's cleanest cities.",
            "Street-food nights here are famous nationwide.",
        ),
        "city_istanbul" to listOf(
            "Istanbul straddles two continents — Europe and Asia!",
            "Grand bazaars sell spices, lamps, and sweets.",
        ),
        "city_jaipur" to listOf(
            "Jaipur is the Pink City — many buildings are painted terracotta pink!",
            "It's famous for forts perched on desert hills.",
        ),
        "city_kanpur" to listOf(
            "Kanpur grew as a busy industrial city on the Ganges plain.",
            "Wide riverbanks make big festival gatherings.",
        ),
        "city_kolkata" to listOf(
            "Kolkata loves books, trams, and grand old buildings.",
            "Howrah Bridge stretches like a steel giant over the river.",
        ),
        "city_lagos" to listOf(
            "Lagos is one of Africa's biggest, busiest cities.",
            "Lagoon bridges link islands packed with music and markets.",
        ),
        "city_lahore" to listOf(
            "Lahore's Badshahi Mosque and fort glow at sunset.",
            "Food streets sizzle with kebabs and sweet jalebi.",
        ),
        "city_lima" to listOf(
            "Lima sits on Pacific cliffs in a misty desert climate.",
            "It's a capital of amazing ceviche seafood.",
        ),
        "city_london" to listOf(
            "London's Big Ben clock tower is a world celebrity.",
            "Black cabs and red buses are city icons.",
        ),
        "city_lucknow" to listOf(
            "Lucknow is known for polite manners and delicate embroidery.",
            "Its kebabs and fragrant rice are legendary.",
        ),
        "city_lyon" to listOf(
            "Lyon is a French food capital with secret covered passages.",
            "Two rivers meet beside Roman ruins and silk history.",
        ),
        "city_moscow" to listOf(
            "Moscow's Red Square faces colorful onion-dome cathedrals.",
            "The Metro stations look like underground palaces.",
        ),
        "city_mumbai" to listOf(
            "Mumbai is built on what were once seven islands stitched together.",
            "Bollywood movies light up screens from here.",
        ),
        "city_mysuru" to listOf(
            "Mysuru Palace lights up with thousands of bulbs on festival nights.",
            "The city is famous for soft silk and sweet Mysore pak.",
        ),
        "city_nagoya" to listOf(
            "Nagoya Castle's golden dolphins shine on the roof.",
            "Bullet trains whoosh through this industrial hub.",
        ),
        "city_nagpur" to listOf(
            "Nagpur is called the Orange City for its juicy oranges.",
            "It's near the geographic center of India.",
        ),
        "city_nairobi" to listOf(
            "Nairobi has a national park where giraffes roam near skyscrapers!",
            "It's the capital of Kenya in East Africa.",
        ),
        "city_naples" to listOf(
            "Naples claims the birthplace of pizza margherita!",
            "Vesuvius volcano watches over the bay.",
        ),
        "city_nashville" to listOf(
            "Nashville is Music City — home of country songs.",
            "Neon guitars glow along Broadway.",
        ),
        "city_newyork" to listOf(
            "New York's Statue of Liberty greets ships in the harbor.",
            "Times Square glows with giant screens all night.",
        ),
        "city_nice" to listOf(
            "Nice sparkles on the French Riviera with a pebble beach promenade.",
            "Gelato colors match the Mediterranean light.",
        ),
        "city_nicosia" to listOf(
            "Nicosia is Europe's last divided capital city.",
            "Venetian walls still circle the old town like a star.",
        ),
        "city_odessa" to listOf(
            "Odessa's Potemkin Stairs tumble toward the Black Sea.",
            "It grew as a lively port of many cultures.",
        ),
        "city_orlando" to listOf(
            "Orlando is world-famous for giant theme parks.",
            "Nearby lakes and wetlands host alligators and birds.",
        ),
        "city_osaka" to listOf(
            "Osaka is Japan's kitchen — takoyaki and okonomiyaki heaven.",
            "A huge castle with a green moat sits downtown.",
        ),
        "city_oslo" to listOf(
            "Oslo hides wild forests and fjord beaches inside city limits.",
            "Nobel Peace Prize ceremonies happen here.",
        ),
        "city_ottawa" to listOf(
            "Ottawa's canals become ice-skating trails in winter!",
            "Parliament's Peace Tower watches the city.",
        ),
        "city_paris" to listOf(
            "Paris is home to the Eiffel Tower and crusty baguettes.",
            "Artists have painted its river bridges for centuries.",
        ),
        "city_patna" to listOf(
            "Patna sits where ancient Pataliputra once ruled empires.",
            "The Ganges River curves right past the city.",
        ),
        "city_pune" to listOf(
            "Pune is a student city with many colleges and cycling lanes.",
            "Nearby hills make weekend treks popular.",
        ),
        "city_rabat" to listOf(
            "Rabat is Morocco's calm capital by the Atlantic.",
            "Blue-and-white Kasbah alleys overlook the ocean.",
        ),
        "city_recife" to listOf(
            "Recife means \"reef\" — coral reefs protect its coast.",
            "Bridges lace the city over rivers and islands.",
        ),
        "city_reykjavik" to listOf(
            "Reykjavik is the world's northernmost capital of a country.",
            "Steam from hot springs heats homes and pools.",
        ),
        "city_riga" to listOf(
            "Riga's Old Town is packed with pointed church towers.",
            "Art Nouveau buildings wear faces and flowers on their walls.",
        ),
        "city_riyadh" to listOf(
            "Riyadh rose from a desert oasis into a gleaming capital.",
            "Cool evenings bring families out to parks and souks.",
        ),
        "city_rome" to listOf(
            "Rome's Colosseum once hosted epic ancient shows.",
            "You can still toss a coin in the Trevi Fountain for luck.",
        ),
        "city_rotterdam" to listOf(
            "Rotterdam has bold modern architecture after wartime rebuilding.",
            "Cube houses tilt on stilts like puzzle pieces.",
        ),
        "city_seoul" to listOf(
            "Seoul mixes ancient palaces with K-pop neon.",
            "Han River parks fill with picnics and bike rides.",
        ),
        "city_shimla" to listOf(
            "Shimla was a cool summer capital in the hills.",
            "A toy train chugs up through pine forests.",
        ),
        "city_singapore" to listOf(
            "Singapore's airport has a butterfly garden and a slide!",
            "Gardens by the Bay lights up giant \"supertrees\" at night.",
        ),
        "city_surat" to listOf(
            "Surat sparkles as a huge diamond-polishing center.",
            "It's also famous for fluffy textile markets.",
        ),
        "city_sydney" to listOf(
            "Sydney Opera House looks like giant white sails by the harbor.",
            "Nearby beaches host surfers year-round.",
        ),
        "city_thane" to listOf(
            "Thane is packed with lakes near Mumbai.",
            "It's sometimes called the City of Lakes of Maharashtra.",
        ),
        "city_tokyo" to listOf(
            "Tokyo mixes neon towers with quiet shrine gardens.",
            "Trains arrive so on time you could set a watch.",
        ),
        "city_toronto" to listOf(
            "Toronto's CN Tower once held the world height record.",
            "In summer, islands in the lake become picnic playgrounds.",
        ),
        "city_udaipur" to listOf(
            "Udaipur is the City of Lakes with palaces that seem to float.",
            "Boat rides at sunset feel like fairy tales.",
        ),
        "city_varanasi" to listOf(
            "Varanasi is one of the world's oldest living cities.",
            "Dawn boat rides watch the river ghats wake up.",
        ),
        "city_venice" to listOf(
            "Venice has canals instead of many streets — boats are taxis!",
            "It's built on wooden piles sunk into a lagoon.",
        ),
        "city_vienna" to listOf(
            "Vienna was home to Mozart, Beethoven, and fancy cake shops.",
            "Giant Ferris wheel views watch over the Prater park.",
        ),
        "city_warsaw" to listOf(
            "Warsaw rebuilt its Old Town to look centuries old again.",
            "Chopin's music is celebrated all over the city.",
        ),
        "city_washington" to listOf(
            "Washington, D.C., holds the U.S. Capitol and many free museums.",
            "The National Mall is a giant grassy parade ground.",
        ),
        "city_wellington" to listOf(
            "Wellington is the world's windiest capital!",
            "Cable cars climb to views over a sparkling harbor.",
        ),
        "city_winnipeg" to listOf(
            "Winnipeg sits at the heart of Canada where rivers meet.",
            "A giant polar-bear habitat draws animal fans.",
        ),
        "city_wuhan" to listOf(
            "Wuhan sits where the Yangtze and Han rivers meet.",
            "Cherry blossoms and long bridges define its spring.",
        ),
        "city_yangon" to listOf(
            "Yangon's Shwedagon Pagoda is covered in gold.",
            "Colonial-era buildings line shady downtown streets.",
        ),
        "city_yaounde" to listOf(
            "Yaoundé is Cameroon's hilly green capital.",
            "From ridges you can see neighborhoods spilling over valleys.",
        ),
        "city_yaroslavl" to listOf(
            "Yaroslavl sits where two rivers meet in Russia.",
            "Its old churches are a UNESCO treasure.",
        ),
        "city_yerevan" to listOf(
            "Yerevan is one of the world's oldest capital cities.",
            "Pink volcanic stone makes many buildings glow at sunset.",
        ),
        "city_yogyakarta" to listOf(
            "Yogyakarta is Java's cultural heart with royal palaces.",
            "Nearby Borobudur temple is a stone mountain of Buddhas.",
        ),
        "city_yokohama" to listOf(
            "Yokohama has one of Japan's biggest Chinatowns.",
            "A giant Ferris wheel spins over the harbor.",
        ),
        "city_york" to listOf(
            "York's medieval walls still circle much of the city.",
            "Vikings once called it Jorvik.",
        ),
        "city_yuma" to listOf(
            "Yuma, Arizona, is one of the sunniest cities on Earth!",
            "The Colorado River skirts the desert town.",
        ),
        "country_afghanistan" to listOf(
            "Afghanistan's lapis lazuli blue stone traveled ancient trade routes.",
            "Band-e Amir lakes shine turquoise in the mountains.",
        ),
        "country_albania" to listOf(
            "Albania's beaches hide next to rugged mountain passes.",
            "Bunkers from the past dot the landscape like mushrooms.",
        ),
        "country_algeria" to listOf(
            "Algeria is Africa's biggest country by land area.",
            "Sahara dunes meet Mediterranean coasts.",
        ),
        "country_andorra" to listOf(
            "Andorra is a tiny country high in the Pyrenees.",
            "It has two princes — one in France, one a Spanish bishop!",
        ),
        "country_angola" to listOf(
            "Angola's giant sable antelope is a national symbol.",
            "Atlantic beaches stretch for hundreds of kilometers.",
        ),
        "country_antigua" to listOf(
            "Antigua and Barbuda is famous for powdery beaches.",
            "A different beach for almost every day of the year, locals say.",
        ),
        "country_argentina" to listOf(
            "Argentina's name hints at silver — early explorers hoped for treasure.",
            "Soccer passion and tango dancing light up its cities.",
        ),
        "country_armenia" to listOf(
            "Armenia claims one of the world's oldest churches.",
            "Apricots are so loved they feel like a national fruit.",
        ),
        "country_australia" to listOf(
            "Australia is both a country and a whole continent!",
            "Many animals here — like kangaroos — live nowhere else.",
        ),
        "country_austria" to listOf(
            "Austria's Alps host fairy-tale villages and ski slopes.",
            "Mozart was born in Salzburg.",
        ),
        "country_azerbaijan" to listOf(
            "Azerbaijan has mountains that literally leak fire from gas seeps.",
            "Baku's modern towers rise by the Caspian Sea.",
        ),
        "country_bangladesh" to listOf(
            "Bangladesh is woven with hundreds of rivers.",
            "It's a world leader in soft cotton clothing.",
        ),
        "country_belgium" to listOf(
            "Belgium invented fries (and argues about it happily).",
            "Comic heroes like Tintin started here.",
        ),
        "country_bhutan" to listOf(
            "Bhutan measures Gross National Happiness!",
            "It stayed forest-covered on purpose.",
        ),
        "country_bolivia" to listOf(
            "Bolivia's Uyuni salt flat mirrors the sky like a giant mirror.",
            "It has two capitals for different government jobs.",
        ),
        "country_botswana" to listOf(
            "Botswana's Okavango Delta floods into a wildlife paradise.",
            "Diamonds helped fund parks and schools.",
        ),
        "country_brazil" to listOf(
            "Brazil holds most of the Amazon rainforest.",
            "Carnival fills streets with music, feathers, and dance.",
        ),
        "country_bulgaria" to listOf(
            "Bulgaria's rose valleys perfume the world's fancy oils.",
            "Yogurt cultures here are world-famous.",
        ),
        "country_cambodia" to listOf(
            "Cambodia's Angkor Wat is a temple city of stone towers.",
            "Water festivals race longboats on the Tonlé Sap.",
        ),
        "country_cameroon" to listOf(
            "Cameroon is called Africa in miniature for its many landscapes.",
            "Indomitable Lions soccer fans cheer nationwide.",
        ),
        "country_canada" to listOf(
            "Canada has more lakes than any other country.",
            "Polar bears roam its far northern shores.",
        ),
        "country_chile" to listOf(
            "Chile is a skinny country almost 4,300 km long!",
            "The Atacama Desert is one of Earth's driest places.",
        ),
        "country_china" to listOf(
            "The Great Wall snakes across mountains for thousands of kilometers.",
            "Pandas and inventing paper are part of its fame.",
        ),
        "country_croatia" to listOf(
            "Croatia's coast has over a thousand islands and islets.",
            "Dubrovnik's walls starred in fantasy TV shows.",
        ),
        "country_cuba" to listOf(
            "Cuba is the largest Caribbean island.",
            "Classic colorful cars roll past seaside promenades.",
        ),
        "country_cyprus" to listOf(
            "Cyprus is Aphrodite's mythical island in the Mediterranean.",
            "Halloumi cheese squeaks when you chew it!",
        ),
        "country_denmark" to listOf(
            "Denmark's flag is the oldest national flag still in use!",
            "Legoland began with Danish Lego bricks.",
        ),
        "country_djibouti" to listOf(
            "Djibouti sits at the Red Sea's busy gateway.",
            "Lake Assal is one of Earth's saltiest spots.",
        ),
        "country_dominica" to listOf(
            "Dominica is the Nature Island of the Caribbean.",
            "A boiling lake steams in its volcanic highlands.",
        ),
        "country_dominicanrepublic" to listOf(
            "The Dominican Republic shares an island with Haiti.",
            "Merengue music keeps feet moving.",
        ),
        "country_ecuador" to listOf(
            "Ecuador sits on Earth's equator — you can straddle both hemispheres!",
            "The Galápagos Islands inspired Darwin.",
        ),
        "country_egypt" to listOf(
            "Egypt's pyramids are among the ancient Wonders of the World.",
            "The Nile River made desert life possible.",
        ),
        "country_elsalvador" to listOf(
            "El Salvador is Central America's smallest country.",
            "Pupusas — stuffed corn cakes — are the national comfort food.",
        ),
        "country_england" to listOf(
            "England's Big Ben and red buses star in London postcards.",
            "Football (soccer) rules were shaped here.",
        ),
        "country_eritrea" to listOf(
            "Eritrea's coast faces the Red Sea's diving reefs.",
            "Asmara's buildings look like an Art Deco museum.",
        ),
        "country_estonia" to listOf(
            "Estonia is a digital pioneer — even voting can be online.",
            "Forests and islands cover much of the land.",
        ),
        "country_ethiopia" to listOf(
            "Ethiopia is a birthplace of coffee ceremonies.",
            "It follows its own calendar and clock traditions.",
        ),
        "country_finland" to listOf(
            "Finland is packed with saunas — more than cars in some counts!",
            "It often ranks among the world's happiest countries.",
        ),
        "country_france" to listOf(
            "France gave the world the Eiffel Tower and flaky croissants.",
            "It's the most visited country by travelers.",
        ),
        "country_georgia" to listOf(
            "Georgia (the country) may be the birthplace of wine.",
            "Caucasus mountains guard its valleys.",
        ),
        "country_germany" to listOf(
            "Germany has fairy-tale castles and super-fast Autobahn roads.",
            "It's famous for inventors, music, and football.",
        ),
        "country_ghana" to listOf(
            "Ghana was the first sub-Saharan African country to gain independence in 1957.",
            "Cacao for chocolate grows in its forests.",
        ),
        "country_greece" to listOf(
            "Greece's islands sparkle in the blue Aegean Sea.",
            "Olympics and democracy ideas began here.",
        ),
        "country_guatemala" to listOf(
            "Guatemala's volcanoes tower over Mayan temple cities.",
            "Jade and colorful textiles are national treasures.",
        ),
        "country_honduras" to listOf(
            "Honduras's Copán ruins show intricate Mayan carvings.",
            "Caribbean islands like Roatán lure divers.",
        ),
        "country_hungary" to listOf(
            "Hungary's capital Budapest has thermal bath palaces.",
            "The Danube River splits Buda and Pest.",
        ),
        "country_iceland" to listOf(
            "Iceland sits on hot volcanic ground with geysers and glaciers.",
            "Almost all electricity comes from clean Earth heat and water.",
        ),
        "country_india" to listOf(
            "India celebrates dozens of languages and festivals.",
            "It's where chess and the number zero were nurtured.",
        ),
        "country_indonesia" to listOf(
            "Indonesia has more than 13,000 islands!",
            "Komodo dragons live on some of them.",
        ),
        "country_iran" to listOf(
            "Iran's ancient Persepolis still shows carved stone guardians.",
            "It's a cradle of gardens, poetry, and carpets.",
        ),
        "country_iraq" to listOf(
            "Iraq holds ancient Mesopotamia — land of early writing.",
            "The Tigris and Euphrates rivers shaped civilization.",
        ),
        "country_ireland" to listOf(
            "Ireland is the Emerald Isle for its green hills.",
            "Legends of leprechauns and giant's causeways abound.",
        ),
        "country_israel" to listOf(
            "Israel's Dead Sea is Earth's lowest land point on shore.",
            "Deserts bloom with clever drip irrigation.",
        ),
        "country_italy" to listOf(
            "Italy looks like a boot kicking a soccer ball (Sicily)!",
            "Pizza and pasta made it a food hero.",
        ),
        "country_jamaica" to listOf(
            "Jamaica is the birthplace of reggae music.",
            "Usain Bolt sprinted from here to world records.",
        ),
        "country_japan" to listOf(
            "Japan has thousands of islands and bullet trains.",
            "Cherry blossoms paint parks pink each spring.",
        ),
        "country_jordan" to listOf(
            "Jordan's Petra is a city carved into rose-red cliffs.",
            "The Dead Sea is so salty you float like a cork.",
        ),
        "country_kazakhstan" to listOf(
            "Kazakhstan is the world's largest landlocked country.",
            "Steppe eagles soar over endless grasslands.",
        ),
        "country_kenya" to listOf(
            "Kenya's safaris show lions, elephants, and flamingos.",
            "Champions of long-distance running train in its highlands.",
        ),
        "country_kuwait" to listOf(
            "Kuwait City's towers look like baskets by the Gulf.",
            "Oil wealth built a modern desert capital.",
        ),
        "country_laos" to listOf(
            "Laos is Southeast Asia's only landlocked country.",
            "Buddhist temples line the Mekong River town of Luang Prabang.",
        ),
        "country_latvia" to listOf(
            "Latvia's capital Riga sparkles with Art Nouveau façades.",
            "Wide sandy beaches line the Baltic Sea.",
        ),
        "country_lebanon" to listOf(
            "Lebanon's cedar trees appear on its flag.",
            "Ancient Roman ruins stand at Baalbek.",
        ),
        "country_liberia" to listOf(
            "Liberia's flag echoes the U.S. design — linked by history.",
            "Rainforests shelter pygmy hippos.",
        ),
        "country_lithuania" to listOf(
            "Lithuania was the first Soviet republic to declare independence in 1990.",
            "Amber from the Baltic washes onto its shores.",
        ),
        "country_luxembourg" to listOf(
            "Luxembourg is a tiny country with fairy-tale forts.",
            "It has more bank logos than traffic jams — and deep green forests.",
        ),
        "country_madagascar" to listOf(
            "Madagascar split from India long ago — animals evolved alone!",
            "Lemurs live wild almost nowhere else.",
        ),
        "country_malawi" to listOf(
            "Malawi's lake holds more fish species than almost any lake.",
            "It's nicknamed the Warm Heart of Africa.",
        ),
        "country_malaysia" to listOf(
            "Malaysia's Petronas Towers once ruled the skyline charts.",
            "Rainforests hide orangutans on Borneo.",
        ),
        "country_malta" to listOf(
            "Malta's temples are older than Egypt's pyramids.",
            "Three small islands pack huge history.",
        ),
        "country_mexico" to listOf(
            "Mexico gave the world chocolate, corn, and chilies.",
            "Colorful Día de Muertos celebrations honor ancestors.",
        ),
        "country_mongolia" to listOf(
            "Mongolia has more horses than people in many areas!",
            "Gers (yurts) still house nomad families on the steppe.",
        ),
        "country_morocco" to listOf(
            "Morocco's markets smell of spices, leather, and mint tea.",
            "The Sahara dunes roll to the south.",
        ),
        "country_mozambique" to listOf(
            "Mozambique's coastline stretches over 2,400 km.",
            "Dhow sails still cross turquoise channels.",
        ),
        "country_myanmar" to listOf(
            "Myanmar's Shwedagon Pagoda shines with gold and jewels.",
            "Thousands of temples fill the plains of Bagan.",
        ),
        "country_namibia" to listOf(
            "Namibia's Namib Desert has some of Earth's tallest dunes.",
            "Desert elephants roam dry riverbeds.",
        ),
        "country_nepal" to listOf(
            "Nepal holds Mount Everest, Earth's highest peak.",
            "Prayer flags flutter across Himalayan valleys.",
        ),
        "country_netherlands" to listOf(
            "A third of the Netherlands sits below sea level — protected by dikes!",
            "Tulip fields paint the spring countryside.",
        ),
        "country_nicaragua" to listOf(
            "Nicaragua is laced with volcanoes and twin big lakes.",
            "Freshwater sharks once lived in Lake Nicaragua.",
        ),
        "country_nigeria" to listOf(
            "Nigeria has Africa's biggest population.",
            "Nollywood makes tons of movies each year.",
        ),
        "country_norway" to listOf(
            "Norway's fjords are deep sea arms carved by ice.",
            "In winter, northern lights dance across the sky.",
        ),
        "country_oman" to listOf(
            "Oman's forts watch over desert and sea.",
            "Frankincense trees made it famous on ancient trade routes.",
        ),
        "country_pakistan" to listOf(
            "Pakistan's north holds some of Earth's tallest peaks.",
            "Truck art turns cargo lorries into rolling rainbows.",
        ),
        "country_panama" to listOf(
            "The Panama Canal lets ships shortcut between two oceans.",
            "Rainforests host more bird species than all of Europe.",
        ),
        "country_paraguay" to listOf(
            "Paraguay is one of two landlocked countries in South America.",
            "Guaraní is spoken alongside Spanish every day.",
        ),
        "country_peru" to listOf(
            "Peru's Machu Picchu sits high in the Andes clouds.",
            "Potatoes were first farmed in these mountains.",
        ),
        "country_poland" to listOf(
            "Poland's bison still roam ancient forests.",
            "Chopin's piano music is a national treasure.",
        ),
        "country_portugal" to listOf(
            "Portugal's explorers once mapped half the oceans.",
            "Pastel de nata custard tarts are a national treasure.",
        ),
        "country_qatar" to listOf(
            "Qatar is a small peninsula rich in natural gas.",
            "It hosted the first World Cup in the Middle East.",
        ),
        "country_russia" to listOf(
            "Russia spans 11 time zones — the biggest country by land!",
            "Siberian tigers live in its far eastern forests.",
        ),
        "country_rwanda" to listOf(
            "Rwanda is the Land of a Thousand Hills.",
            "Mountain gorillas draw quiet forest visitors.",
        ),
        "country_senegal" to listOf(
            "Senegal's Pink Lake can turn cotton-candy colored!",
            "Music styles like mbalax shake Dakar nights.",
        ),
        "country_serbia" to listOf(
            "Serbia sits where many European cultures meet.",
            "Rasberry fields make it a fruit-export champion.",
        ),
        "country_singapore" to listOf(
            "Singapore is a city-state — a whole country in one city!",
            "Chewing gum sales are tightly limited to keep streets clean.",
        ),
        "country_slovakia" to listOf(
            "Slovakia is packed with castles and cave secrets.",
            "The High Tatras mountains guard the north.",
        ),
        "country_slovenia" to listOf(
            "Slovenia's Lake Bled has a fairy-tale church island.",
            "It's one of Europe's greenest countries.",
        ),
        "country_somalia" to listOf(
            "Somalia has Africa's longest coastline.",
            "Frankincense and myrrh still grow inland.",
        ),
        "country_spain" to listOf(
            "Spain's flamenco mixes guitar, dance, and song.",
            "It has sunny coasts and snowy mountains in one country.",
        ),
        "country_sudan" to listOf(
            "Sudan holds more pyramids than Egypt — at Meroë!",
            "The Nile's two branches meet near Khartoum.",
        ),
        "country_suriname" to listOf(
            "Suriname is South America's smallest country.",
            "Rainforests cover most of the land — and many languages mix in the capital.",
        ),
        "country_sweden" to listOf(
            "Sweden invented the zipper and safety matches.",
            "Allemansrätt lets people roam nature responsibly.",
        ),
        "country_switzerland" to listOf(
            "Switzerland is famous for mountains, chocolate, and neutrality.",
            "Four national languages share one small country.",
        ),
        "country_tanzania" to listOf(
            "Tanzania guards Mount Kilimanjaro and Serengeti herds.",
            "Zanzibar's spice islands scent the Indian Ocean.",
        ),
        "country_thailand" to listOf(
            "Thailand is the Land of Smiles with golden temples.",
            "Floating markets sell fruit from wooden boats.",
        ),
        "country_tunisia" to listOf(
            "Tunisia's Star Wars desert towns look otherworldly.",
            "Ancient Carthage ruins face the Mediterranean.",
        ),
        "country_turkey" to listOf(
            "Turkey bridges Europe and Asia across the Bosporus.",
            "Hot-air balloons float over fairy chimneys in Cappadocia.",
        ),
        "country_uganda" to listOf(
            "Uganda is called the Pearl of Africa.",
            "Mountain gorillas live in its misty forests.",
        ),
        "country_ukraine" to listOf(
            "Ukraine's flag mirrors blue sky over golden wheat fields.",
            "It's one of the world's great breadbaskets.",
        ),
        "country_uruguay" to listOf(
            "Uruguay made a early national constitution in South America.",
            "Beaches and grass-fed cattle define much of life.",
        ),
        "country_uzbekistan" to listOf(
            "Uzbekistan's Silk Road cities glow with blue-tiled mosques.",
            "Samarkand was a jewel of ancient trade.",
        ),
        "country_venezuela" to listOf(
            "Venezuela's Angel Falls is the world's highest waterfall.",
            "Oil and Caribbean coasts shape its story.",
        ),
        "country_vietnam" to listOf(
            "Vietnam's Ha Long Bay is filled with limestone islands.",
            "Pho noodle soup warms mornings nationwide.",
        ),
        "country_wales" to listOf(
            "Wales has more castles per person than almost anywhere!",
            "Dragons decorate the national flag.",
        ),
        "country_yemen" to listOf(
            "Yemen's old city of Sana'a has tower houses like gingerbread.",
            "It's famous for fragrant coffee history.",
        ),
        "country_zambia" to listOf(
            "Zambia shares Victoria Falls — smoke that thunders!",
            "Safari parks protect walking lions and wild dogs.",
        ),
        "country_zimbabwe" to listOf(
            "Zimbabwe shares Victoria Falls' thundering curtain.",
            "Balancing rock formations look like giant sculptures.",
        ),
        "flower_aster" to listOf(
            "Asters bloom late — like fireworks at summer's end.",
            "Their name means \"star\" in Greek.",
        ),
        "flower_azalea" to listOf(
            "Azaleas explode into pink and red spring clouds.",
            "They're cousins of rhododendrons.",
        ),
        "flower_begonia" to listOf(
            "Begonias have colorful leaves as showy as their flowers.",
            "Many are happy in shady spots.",
        ),
        "flower_bluebell" to listOf(
            "Bluebell woods carpet the ground in blue each spring.",
            "Their bells nod on thin stems in the breeze.",
        ),
        "flower_camellia" to listOf(
            "Camellias bloom in winter when other flowers sleep.",
            "Tea leaves come from a camellia relative.",
        ),
        "flower_carnation" to listOf(
            "Carnation petals have frilly zigzag edges.",
            "Different colors mean different friendly messages.",
        ),
        "flower_crocus" to listOf(
            "Crocuses poke through late snow to announce spring.",
            "Saffron spice comes from crocus stigmas!",
        ),
        "flower_daffodil" to listOf(
            "Daffodils are spring's yellow trumpets.",
            "The center cup is called a corona.",
        ),
        "flower_dahlia" to listOf(
            "Dahlias were first grown in Mexico.",
            "Blooms can be tiny pom-poms or dinner-plate huge!",
        ),
        "flower_daisy" to listOf(
            "Daisy centers are packed with tiny flowers — not one bloom!",
            "Kids love making daisy chains.",
        ),
        "flower_dandelion" to listOf(
            "Dandelion clocks are seed parachutes kids love to blow.",
            "Bees feast on the yellow flowers early in spring.",
        ),
        "flower_edelweiss" to listOf(
            "Edelweiss grows high on rocky Alpine cliffs.",
            "Its fuzzy star flowers feel like soft wool.",
        ),
        "flower_elderflower" to listOf(
            "Elderflowers make sparkling cordial and fragrant fritters.",
            "The same tree later grows dark elderberries.",
        ),
        "flower_foxglove" to listOf(
            "Foxglove towers wear speckled glove-like bells.",
            "They're beautiful — and not for tasting.",
        ),
        "flower_gardenia" to listOf(
            "Gardenias smell like creamy perfume factories.",
            "Their shiny leaves stay green year-round in warm places.",
        ),
        "flower_geranium" to listOf(
            "Garden geraniums (pelargoniums) love sunny windowsills.",
            "Some leaves smell like lemon, rose, or mint!",
        ),
        "flower_hibiscus" to listOf(
            "Hibiscus flowers can be bigger than your hand.",
            "Some countries put hibiscus on their flags or tea cups.",
        ),
        "flower_hyacinth" to listOf(
            "Hyacinths pack a punch of sweet scent from dense spikes.",
            "Bulbs can be forced to bloom indoors in winter.",
        ),
        "flower_iris" to listOf(
            "Iris flowers are named after the Greek rainbow goddess.",
            "Their leaves grow like green swords.",
        ),
        "flower_jasmine" to listOf(
            "Jasmine releases its strongest perfume at night.",
            "People weave it into garlands for celebrations.",
        ),
        "flower_lavender" to listOf(
            "Lavender's smell helps many people feel calm.",
            "Bees go crazy for its purple spikes.",
        ),
        "flower_lilac" to listOf(
            "Lilacs perfume whole neighborhoods in spring.",
            "Flower clusters are called panicles.",
        ),
        "flower_lily" to listOf(
            "Lilies grow from underground bulbs.",
            "Some kinds smell so sweet a whole room notices.",
        ),
        "flower_lotus" to listOf(
            "Lotus flowers rise clean from muddy water.",
            "Seeds can stay alive for hundreds of years!",
        ),
        "flower_magnolia" to listOf(
            "Magnolias are ancient — they bloomed with dinosaurs!",
            "Fuzzy buds open into big waxy cups.",
        ),
        "flower_marigold" to listOf(
            "Marigolds' bright orange smells keep some garden bugs away.",
            "They're common in Indian festival garlands.",
        ),
        "flower_narcissus" to listOf(
            "Narcissus is the fancy family name for daffodils.",
            "In myth, Narcissus stared at his reflection in water.",
        ),
        "flower_nigella" to listOf(
            "Nigella is also called love-in-a-mist for its lacy green collar.",
            "Seed pods look like little striped lanterns.",
        ),
        "flower_orchid" to listOf(
            "Orchids are one of the biggest plant families on Earth.",
            "Vanilla flavor comes from an orchid seed pod!",
        ),
        "flower_peony" to listOf(
            "Peony buds are as round as softballs before they burst open.",
            "Ants often visit for sweet bud nectar — they don't hurt the flower.",
        ),
        "flower_periwinkle" to listOf(
            "Periwinkle vines carpet shady ground with blue stars.",
            "They stay green almost all year.",
        ),
        "flower_petunia" to listOf(
            "Petunias pour color from hanging baskets all summer.",
            "They're cousins of tomatoes and potatoes!",
        ),
        "flower_poppy" to listOf(
            "Poppy petals are as thin as tissue paper.",
            "Some seed pods make the tiny seeds on bread rolls.",
        ),
        "flower_primrose" to listOf(
            "Primroses are among the first flowers of spring.",
            "Their name means \"first rose.\"",
        ),
        "flower_rose" to listOf(
            "Roses have been garden favorites for thousands of years.",
            "Wild roses often have five petals — fancy ones have dozens.",
        ),
        "flower_snapdragon" to listOf(
            "Squeeze a snapdragon and the \"mouth\" opens and closes!",
            "Seed pods look like tiny skulls — in a cute way.",
        ),
        "flower_sunflower" to listOf(
            "Young sunflowers turn their faces to follow the sun!",
            "One head can hold hundreds of seeds.",
        ),
        "flower_tulip" to listOf(
            "Tulips came to Europe from Turkey and Central Asia.",
            "During \"tulip mania,\" rare bulbs cost more than houses!",
        ),
        "flower_violet" to listOf(
            "Violets often hide their flowers low in the grass.",
            "Some candy and syrups are violet-flavored.",
        ),
        "flower_yarrow" to listOf(
            "Yarrow has flat clusters of tiny flowers.",
            "Feathery leaves smell herby when crushed.",
        ),
        "flower_ylangylang" to listOf(
            "Ylang-ylang flowers smell so sweet they're used in fancy perfumes.",
            "The tree grows in warm tropical places.",
        ),
        "flower_yucca" to listOf(
            "Yucca sends up tall towers of creamy bells.",
            "Special moths help yucca plants make seeds.",
        ),
        "flower_zinnia" to listOf(
            "Zinnias come in almost every crayon color.",
            "Butterflies treat them like a nectar café.",
        ),
        "fruit_ackee" to listOf(
            "Ackee is Jamaica's national fruit.",
            "Only the soft yellow bits are eaten — and only when ripe!",
        ),
        "fruit_apple" to listOf(
            "An apple floats because it's about 25% air!",
            "The tiny seeds sit in a star shape if you cut one the right way.",
        ),
        "fruit_apricot" to listOf(
            "Apricots are fuzzy cousins of peaches.",
            "Ancient people carried apricot trees along the Silk Road.",
        ),
        "fruit_avocado" to listOf(
            "Avocados have one giant seed in the middle.",
            "They're creamy fruits often mashed into guacamole.",
        ),
        "fruit_banana" to listOf(
            "Bananas are berries — but strawberries aren't!",
            "They're curved because they grow toward the sun.",
        ),
        "fruit_blackberry" to listOf(
            "Blackberries are clusters of tiny juice bubbles stuck together.",
            "Wild vines can be full of prickles — and treasure!",
        ),
        "fruit_blueberry" to listOf(
            "Blueberries get a dusty white \"bloom\" that keeps them fresh.",
            "They start out green and turn deep blue when ripe.",
        ),
        "fruit_cherry" to listOf(
            "One cherry tree can make thousands of cherries!",
            "Cherry pits hide inside like little stones.",
        ),
        "fruit_coconut" to listOf(
            "Coconuts can float across oceans to new islands!",
            "You can drink the water and eat the white meat inside.",
        ),
        "fruit_cranberry" to listOf(
            "Cranberries bounce when they're ripe — farmers test them!",
            "They grow in wet bogs and float during harvest.",
        ),
        "fruit_custardapple" to listOf(
            "Custard apples have creamy flesh that tastes like dessert.",
            "You scoop the sweet bits around the shiny seeds.",
        ),
        "fruit_date" to listOf(
            "Dates grow in huge bunches high on palm trees.",
            "They're naturally super sweet — like candy from a tree.",
        ),
        "fruit_dragonfruit" to listOf(
            "Dragonfruit grows on a climbing cactus!",
            "Inside, tiny black seeds crunch in bright pink or white flesh.",
        ),
        "fruit_elderberry" to listOf(
            "Elderberries make dark purple juice and syrups.",
            "Flowers of the same plant smell sweet in spring.",
        ),
        "fruit_fig" to listOf(
            "A fig is like a flower that bloomed inside-out!",
            "Tiny wasps help some fig trees make fruit.",
        ),
        "fruit_gooseberry" to listOf(
            "Gooseberries can be tart green or sweet pink jewels.",
            "They grow on prickly bushes.",
        ),
        "fruit_grape" to listOf(
            "Grapes grow in bunches on climbing vines.",
            "Dried grapes become raisins — little sweet chews!",
        ),
        "fruit_guava" to listOf(
            "Guavas have more vitamin C than many oranges.",
            "You can eat the soft seeds right along with the fruit.",
        ),
        "fruit_honeydew" to listOf(
            "Honeydew melons have pale green sweet flesh.",
            "A ripe one feels heavy for its size and smells floral.",
        ),
        "fruit_jackfruit" to listOf(
            "Jackfruit is the world's biggest tree fruit — huge!",
            "Its smell is strong but the yellow bulbs taste sweet.",
        ),
        "fruit_kiwi" to listOf(
            "Kiwifruit hides bright green flesh under fuzzy brown skin.",
            "It's named after New Zealand's kiwi bird.",
        ),
        "fruit_kumquat" to listOf(
            "Kumquats are eaten peel and all — sweet skin, tart inside!",
            "They're tiny oval citrus fruits.",
        ),
        "fruit_lemon" to listOf(
            "Lemon juice is so sour it makes your face scrunch!",
            "Sailors once ate lemons to stay healthy on long trips.",
        ),
        "fruit_lychee" to listOf(
            "Lychees wear a bumpy pink shell you peel off.",
            "Inside is a juicy white ball that tastes floral-sweet.",
        ),
        "fruit_mango" to listOf(
            "Mangoes are one of the world's most loved fruits.",
            "In India, mango season feels like a big celebration.",
        ),
        "fruit_melon" to listOf(
            "Melons are mostly water — perfect for hot days!",
            "A ripe melon often smells sweet at the stem end.",
        ),
        "fruit_mulberry" to listOf(
            "Mulberry juice stains fingers purple in seconds!",
            "Silkworms love mulberry leaves.",
        ),
        "fruit_nashi" to listOf(
            "Nashi are crisp Asian pears that crunch like apples.",
            "They're round and often gift-wrapped in Asia.",
        ),
        "fruit_nectarine" to listOf(
            "Nectarines are peaches without the fuzzy jackets.",
            "One gene change makes the skin smooth!",
        ),
        "fruit_olive" to listOf(
            "Olives are pressed to make golden olive oil.",
            "Fresh off the tree they're too bitter — people cure them first.",
        ),
        "fruit_orange" to listOf(
            "Oranges are packed with vitamin C for strong bodies.",
            "The peel's oils make your fingers smell citrusy.",
        ),
        "fruit_papaya" to listOf(
            "Papaya seeds look like peppercorns and are edible!",
            "The orange flesh is soft like sunny butter.",
        ),
        "fruit_passionfruit" to listOf(
            "Passion fruit hides crunchy seeds in tropical goo.",
            "The purple shell wrinkles when it's perfectly ripe.",
        ),
        "fruit_peach" to listOf(
            "Peach fuzz helps protect the fruit's soft skin.",
            "A peach pit has a seed inside that looks like an almond.",
        ),
        "fruit_pear" to listOf(
            "Pears ripen from the inside out.",
            "Their shape is like a lightbulb or a teardrop.",
        ),
        "fruit_persimmon" to listOf(
            "Ripe persimmons taste like honeyed pudding.",
            "Unripe ones make your mouth feel weirdly dry!",
        ),
        "fruit_pineapple" to listOf(
            "Pineapples take almost two years to grow one fruit!",
            "Each \"eye\" on the outside was once a tiny flower.",
        ),
        "fruit_plum" to listOf(
            "Plums can be purple, red, yellow, or green.",
            "Dried plums are called prunes.",
        ),
        "fruit_pomegranate" to listOf(
            "Pomegranates hide hundreds of juicy ruby seeds.",
            "Ancient stories say they mean good luck and plenty.",
        ),
        "fruit_pomelo" to listOf(
            "Pomelos are the giant grandparents of grapefruits.",
            "Thick peels hide mild sweet-tart segments.",
        ),
        "fruit_rambutan" to listOf(
            "Rambutans look spiky but the hairs are soft!",
            "Peel them to find a grape-like translucent fruit.",
        ),
        "fruit_raspberry" to listOf(
            "Raspberries are hollow when you pick them — like tiny cups.",
            "Each bump is a little fruitlet full of juice.",
        ),
        "fruit_sapota" to listOf(
            "Sapota (chikoo) tastes like brown sugar and caramel.",
            "The tree's milky sap was once used in chewing gum.",
        ),
        "fruit_starfruit" to listOf(
            "Starfruit slices look like little yellow stars.",
            "The whole fruit is edible — skin and all.",
        ),
        "fruit_strawberry" to listOf(
            "Strawberry seeds grow on the outside — about 200 per berry!",
            "They're the only fruit with seeds on their skin like that.",
        ),
        "fruit_tamarind" to listOf(
            "Tamarind pods hide sticky sweet-sour pulp.",
            "It's the tangy secret in many chutneys and drinks.",
        ),
        "fruit_tangelo" to listOf(
            "Tangelos are a mix of tangerine and pomelo or grapefruit.",
            "They're juicy with a little knob on top.",
        ),
        "fruit_tangerine" to listOf(
            "Tangerines peel easier than big oranges — kid-friendly!",
            "They're named after the city of Tangier.",
        ),
        "fruit_watermelon" to listOf(
            "Watermelons are over 90% water — nature's juice box!",
            "Some are so big you need two hands to carry them.",
        ),
        "fruit_yuzu" to listOf(
            "Yuzu is a fragrant Japanese citrus with bumpy skin.",
            "Chefs love its zippy aroma in food and drinks.",
        ),
        "vegetable_artichoke" to listOf(
            "We eat the flower bud of the artichoke before it blooms!",
            "The fuzzy center is called the choke — don't eat that part.",
        ),
        "vegetable_arugula" to listOf(
            "Arugula (rocket) has a peppery, nutty bite.",
            "It's a favorite in fresh pizza salads.",
        ),
        "vegetable_asparagus" to listOf(
            "Asparagus spears can grow several centimeters in a day!",
            "They're young shoots of a tall feathery plant.",
        ),
        "vegetable_beans" to listOf(
            "Beans are seeds that pack plant protein.",
            "Climbing beans twirl up poles like living spirals.",
        ),
        "vegetable_beetroot" to listOf(
            "Beetroot juice can turn smoothies (and tongues) bright pink!",
            "Sugar was once made from special sugar beets.",
        ),
        "vegetable_bokchoy" to listOf(
            "Bok choy has crunchy white stems and soft green leaves.",
            "It cooks in minutes in a hot pan.",
        ),
        "vegetable_broccoli" to listOf(
            "Broccoli florets are tiny flower buds you eat before they bloom!",
            "It loves cool weather more than hot summers.",
        ),
        "vegetable_broccolini" to listOf(
            "Broccolini is a tasty mix of broccoli and Chinese kale.",
            "Long tender stems make it easy for kids to eat.",
        ),
        "vegetable_cabbage" to listOf(
            "A cabbage is a giant bud of layered leaves.",
            "Sauerkraut is cabbage fermented into a tangy snack.",
        ),
        "vegetable_capsicum" to listOf(
            "Capsicums (bell peppers) can be green, yellow, orange, or red.",
            "Red ones stayed on the plant longer and turned sweeter.",
        ),
        "vegetable_carrot" to listOf(
            "Carrots were purple and yellow long before they were orange!",
            "Eating them won't give you night-vision superpowers — but they are healthy.",
        ),
        "vegetable_cassava" to listOf(
            "Cassava is a key food across Africa, Asia, and South America.",
            "Tapioca pudding starts with cassava starch.",
        ),
        "vegetable_cauliflower" to listOf(
            "Cauliflower is a cluster of tight white flower buds.",
            "You can even find orange and purple kinds!",
        ),
        "vegetable_celery" to listOf(
            "Celery stalks are crunchy water pipes for the plant.",
            "The leaves are edible and taste herby too.",
        ),
        "vegetable_chard" to listOf(
            "Rainbow chard has stems in pink, yellow, and orange!",
            "Leaves taste a bit like mild spinach.",
        ),
        "vegetable_chickpea" to listOf(
            "Chickpeas become hummus when blended creamy.",
            "They're also called garbanzo beans.",
        ),
        "vegetable_chili" to listOf(
            "Chili heat comes from a chemical called capsaicin.",
            "Birds don't feel the burn — so they spread the seeds!",
        ),
        "vegetable_corn" to listOf(
            "Each corn silk strand connects to one kernel.",
            "Popcorn explodes because water inside turns to steam.",
        ),
        "vegetable_cucumber" to listOf(
            "Cucumbers are over 95% water — crunchy hydration!",
            "They're botanically fruits, but we cook them as veggies.",
        ),
        "vegetable_drumstick" to listOf(
            "Drumstick (moringa) pods are long and green like sticks.",
            "Leaves of the same tree are super nutritious.",
        ),
        "vegetable_edamame" to listOf(
            "Edamame are young green soybeans in fuzzy pods.",
            "You squeeze the beans straight into your mouth.",
        ),
        "vegetable_endive" to listOf(
            "Endive is grown in the dark to keep leaves pale and mild.",
            "It adds a gentle bitter crunch to salads.",
        ),
        "vegetable_enoki" to listOf(
            "Enoki mushrooms look like tiny white noodles with caps.",
            "They love growing on tree stumps.",
        ),
        "vegetable_fennel" to listOf(
            "Fennel bulbs taste lightly like licorice.",
            "Feathery tops look like dill and smell sweet.",
        ),
        "vegetable_garlic" to listOf(
            "Garlic cloves are sections of an underground bulb.",
            "Crushing garlic releases its famous strong smell.",
        ),
        "vegetable_ginger" to listOf(
            "Ginger is a spicy underground stem called a rhizome.",
            "It can settle tummies and warm up tea.",
        ),
        "vegetable_kale" to listOf(
            "Kale stays sweet even after a light frost.",
            "Its curly leaves are vitamin powerhouses.",
        ),
        "vegetable_leek" to listOf(
            "Leeks are gentle onion cousins with long white stems.",
            "Farmers pile soil around them to keep stems pale and tender.",
        ),
        "vegetable_lentil" to listOf(
            "Lentils cook fast and come in green, brown, red, and black.",
            "They're one of the oldest farmed foods on Earth.",
        ),
        "vegetable_lettuce" to listOf(
            "Lettuce leaves are packed with water and crunch.",
            "Romaine, butterhead, and iceberg are different leaf styles.",
        ),
        "vegetable_mushroom" to listOf(
            "Mushrooms are fungi — not plants!",
            "The part we eat is just the \"fruit\"; the rest lives underground.",
        ),
        "vegetable_napacabbage" to listOf(
            "Napa cabbage has crinkly pale leaves perfect for kimchi.",
            "It grows in upright oval heads.",
        ),
        "vegetable_okra" to listOf(
            "Okra gets pleasantly slimy when cooked — great for thickening soups!",
            "Flowers look like pretty hibiscus blooms.",
        ),
        "vegetable_onion" to listOf(
            "Onions make you cry because they release a tear gas–like vapor!",
            "Layers of the bulb are modified leaves.",
        ),
        "vegetable_parsley" to listOf(
            "Parsley is more than a plate decoration — it's full of flavor!",
            "There are curly and flat-leaf kinds.",
        ),
        "vegetable_parsnip" to listOf(
            "Parsnips get sweeter after cold weather.",
            "They look like white carrots with a nutty taste.",
        ),
        "vegetable_peas" to listOf(
            "Peas grow in green pods you can pop open.",
            "Each pod holds a neat row of round seeds.",
        ),
        "vegetable_potato" to listOf(
            "Potatoes are underground stems called tubers — not roots!",
            "The Incas grew them high in the Andes first.",
        ),
        "vegetable_pumpkin" to listOf(
            "Giant pumpkins can weigh more than a small car!",
            "We carve them and roast the seeds for snacks.",
        ),
        "vegetable_radish" to listOf(
            "Radishes grow ready to eat in just a few weeks.",
            "Their spicy crunch wakes up salads.",
        ),
        "vegetable_shallot" to listOf(
            "Shallots grow in clusters like garlic but taste milder than onions.",
            "Chefs love them for delicate sauces.",
        ),
        "vegetable_spinach" to listOf(
            "Spinach leaves are packed with iron and vitamins.",
            "Cartoon sailors made it famous — and it's still a power veggie.",
        ),
        "vegetable_squash" to listOf(
            "Squash comes in summer (zucchini) and winter (butternut) types.",
            "Some gourds were used as bottles long ago.",
        ),
        "vegetable_taro" to listOf(
            "Taro root is used in poi and bubble-tea flavors.",
            "Its big \"elephant ear\" leaves are dramatic in gardens.",
        ),
        "vegetable_thyme" to listOf(
            "Thyme is a tiny-leaf herb that smells woody and warm.",
            "Bees adore thyme flowers.",
        ),
        "vegetable_turnip" to listOf(
            "Turnips have peppery white or purple roots.",
            "People eat both the root and the leafy tops.",
        ),
        "vegetable_watercress" to listOf(
            "Watercress grows in clean flowing streams.",
            "Peppery leaves are packed with vitamins.",
        ),
        "vegetable_yam" to listOf(
            "True yams are starchy tropical tubers with rough skin.",
            "They're different from the sweet potatoes many people call yams.",
        ),
        "vegetable_yardlongbean" to listOf(
            "Yardlong beans can grow as long as a school ruler!",
            "They're popular in Asian stir-fries.",
        ),
        "vegetable_yuca" to listOf(
            "Yuca (cassava) makes fluffy fries and tapioca pearls.",
            "It must be cooked properly before eating.",
        ),
        "vegetable_zucchini" to listOf(
            "Zucchini can grow huge if you forget to pick them!",
            "Flowers of the plant are edible and delicious fried.",
        )
    )
}

