package com.radix2.llm.domain

/**
 * Two unique kid-friendly fun facts per word (keyed by [Word.id]).
 * Round 1 facts lean into what an Indian child would find familiar.
 */
object FunFacts {
    fun forId(id: String): List<String>? = facts[id]

    private val facts: Map<String, List<String>> = mapOf(
        "animal_ant" to listOf(
            "Ants can lift things many times their own weight!",
            "They live in big busy colonies and work as a team.",
        ),
        "animal_antelope" to listOf(
            "Antelopes are super-fast runners on open grasslands.",
            "Many kinds live in India, including the blackbuck.",
        ),
        "animal_ape" to listOf(
            "Apes are our closest animal cousins and can use simple tools.",
            "Unlike monkeys, apes have no tails!",
        ),
        "animal_bat" to listOf(
            "Bats use echoes to find their way in the dark!",
            "Most bats gobble insects and help farmers.",
        ),
        "animal_bear" to listOf(
            "Sloth bears in India love to eat ants and termites.",
            "Bears have a nose better than most dogs.",
        ),
        "animal_boar" to listOf(
            "Wild boars dig the ground with strong snouts.",
            "They live in forests across India.",
        ),
        "animal_buffalo" to listOf(
            "Water buffaloes love cool muddy wallows.",
            "They help plough fields in many Indian villages.",
        ),
        "animal_bull" to listOf(
            "Bulls are strong male cattle.",
            "They have an amazing sense of smell for finding grass.",
        ),
        "animal_camel" to listOf(
            "Camels store fat in their humps — not water bottles!",
            "They are called ships of the desert in Rajasthan.",
        ),
        "animal_cat" to listOf(
            "Cats purr when they feel happy and safe.",
            "A cat's whiskers help it measure tight spaces.",
        ),
        "animal_chameleon" to listOf(
            "Chameleons can look different ways with each eye!",
            "Their long sticky tongues catch bugs in a flash.",
        ),
        "animal_cheetah" to listOf(
            "Cheetahs are the fastest land animals!",
            "Asiatic cheetahs once lived in India long ago.",
        ),
        "animal_chimpanzee" to listOf(
            "Chimps use sticks and stones as simple tools.",
            "They laugh when they play and hug their friends!",
        ),
        "animal_cobra" to listOf(
            "Cobras raise a hood to look bigger when scared.",
            "The spectacled cobra has mark like glasses on its hood.",
        ),
        "animal_cow" to listOf(
            "Cows have four stomach parts to digest grass!",
            "In India, cows are cared for as special animals.",
        ),
        "animal_crab" to listOf(
            "Crabs often walk sideways and wear hard shell armor.",
            "You can spot them on Indian beaches and mangroves.",
        ),
        "animal_crocodile" to listOf(
            "Crocodiles hold their breath underwater a long time.",
            "Mugger crocodiles live in many Indian rivers.",
        ),
        "animal_deer" to listOf(
            "Deer grow brand-new antlers every year.",
            "Spotted deer are common in Indian forests and parks.",
        ),
        "animal_dog" to listOf(
            "A dog's nose print is as unique as a fingerprint!",
            "Street dogs and pet dogs are part of many Indian cities.",
        ),
        "animal_dolphin" to listOf(
            "Dolphins chat with clicks and whistles underwater.",
            "Gangetic river dolphins live in the Ganga!",
        ),
        "animal_donkey" to listOf(
            "Donkeys have huge ears that hear tiny sounds far away.",
            "They carefully carry loads on village paths.",
        ),
        "animal_earthworm" to listOf(
            "Earthworms make soil soft and rich for plants.",
            "Gardeners call them friends of the soil.",
        ),
        "animal_earwig" to listOf(
            "Earwigs have pincers on their tails for defense.",
            "Despite the name, they do not live in ears!",
        ),
        "animal_echidna" to listOf(
            "Echidnas lay eggs — rare for mammals!",
            "You might meet one in a zoo's Australia section.",
        ),
        "animal_eel" to listOf(
            "Some eels can make a shocking zap!",
            "Eels wiggle like snakes in water.",
        ),
        "animal_elephant" to listOf(
            "Elephants never forget their friends.",
            "Baby elephants suck their trunks like thumbs!",
        ),
        "animal_ferret" to listOf(
            "Ferrets are playful cousins of weasels and otters.",
            "They love tunnels and will nap in cozy piles!",
        ),
        "animal_fox" to listOf(
            "Foxes use bushy tails as warm blankets.",
            "The Indian fox lives in grasslands and scrub.",
        ),
        "animal_frog" to listOf(
            "Frogs drink water through their skin!",
            "Monsoon rains bring lots of frog songs at night.",
        ),
        "animal_gaur" to listOf(
            "Gaur are the biggest wild cattle in the world!",
            "They live in Indian forests and are very strong.",
        ),
        "animal_gazelle" to listOf(
            "Gazelles spring high when they run.",
            "The chinkara is a graceful Indian gazelle.",
        ),
        "animal_gecko" to listOf(
            "Geckos climb walls with sticky toe pads!",
            "Many Indian homes hear tiny gecko chirps at night.",
        ),
        "animal_giraffe" to listOf(
            "Giraffes have purple tongues almost as long as your arm!",
            "Kids love meeting them at the zoo.",
        ),
        "animal_goat" to listOf(
            "Goats have rectangular pupils that help them see sideways.",
            "They climb rocks and nibble almost anything.",
        ),
        "animal_gorilla" to listOf(
            "Gorillas make soft leafy nests every night.",
            "They are gentle giants you can see at big zoos.",
        ),
        "animal_guineapig" to listOf(
            "Guinea pigs talk with soft squeaks and rumbles.",
            "They need friends — they love living in little groups!",
        ),
        "animal_hamster" to listOf(
            "Hamsters stuff food in big cheek pouches.",
            "They run on wheels at night while you sleep.",
        ),
        "animal_hare" to listOf(
            "Hares can leap away faster than many dogs can run.",
            "The black-naped hare lives in Indian fields.",
        ),
        "animal_hedgehog" to listOf(
            "Hedgehogs roll into a prickly ball when scared.",
            "They crunch insects for dinner.",
        ),
        "animal_hippo" to listOf(
            "Hippos make their own sunscreen goo!",
            "They cool off in water all day at the zoo.",
        ),
        "animal_horse" to listOf(
            "Horses can sleep standing up!",
            "They talk with ear wiggles, snorts, and neighs.",
        ),
        "animal_hyena" to listOf(
            "Hyenas make funny whoops and giggles.",
            "Striped hyenas live in parts of India.",
        ),
        "animal_iguana" to listOf(
            "Iguanas love sunny rocks for warming up.",
            "They can drop a tail to escape — then grow a new one!",
        ),
        "animal_jackal" to listOf(
            "Jackals call with eerie howls at night.",
            "They often hunt and roam near villages.",
        ),
        "animal_kangaroo" to listOf(
            "Baby joeys grow in Mom's pouch!",
            "Kangaroos are star animals in zoo visits.",
        ),
        "animal_koala" to listOf(
            "Koalas sleep up to 20 hours a day in eucalyptus trees.",
            "They are marsupials — babies ride in a pouch!",
        ),
        "animal_lamb" to listOf(
            "Lambs know Mom's voice and bleat right back.",
            "Soft wool grows into warm sweaters.",
        ),
        "animal_langur" to listOf(
            "Gray langurs leap through temple trees in India.",
            "They have long tails for balancing.",
        ),
        "animal_leopard" to listOf(
            "Leopards haul dinner up trees to keep it safe.",
            "They live quietly in many Indian forests.",
        ),
        "animal_lion" to listOf(
            "Lionesses do most of the hunting for the pride.",
            "Asiatic lions live only in Gir Forest, India!",
        ),
        "animal_lizard" to listOf(
            "Many lizards grow a new tail if they lose one.",
            "Garden lizards do push-ups in the sun.",
        ),
        "animal_llama" to listOf(
            "Llamas hum to chat with their herd.",
            "They carry packs high in the mountains of South America.",
        ),
        "animal_mongoose" to listOf(
            "Mongooses are famous for being brave around snakes.",
            "Rudyard Kipling's Rikki-Tikki-Tavi is a mongoose!",
        ),
        "animal_monkey" to listOf(
            "Monkeys use tails like an extra hand in trees.",
            "Temple monkeys are a familiar sight in India.",
        ),
        "animal_mouse" to listOf(
            "Mice squeeze through holes as small as a pencil!",
            "They nibble grains and scurry at night.",
        ),
        "animal_newt" to listOf(
            "Newts can regrow lost arms and legs!",
            "They live both in water and on land.",
        ),
        "animal_nilgai" to listOf(
            "Nilgai means \"blue cow\" — India's biggest antelope!",
            "Males look bluish-gray from far away.",
        ),
        "animal_octopus" to listOf(
            "Octopuses have three hearts and blue blood!",
            "They squeeze through tiny gaps and change color.",
        ),
        "animal_otter" to listOf(
            "Otters hold hands so they don't drift apart!",
            "Smooth-coated otters swim in Indian rivers.",
        ),
        "animal_ox" to listOf(
            "Oxen pull heavy carts and ploughs.",
            "People have teamed with oxen for thousands of years.",
        ),
        "animal_panda" to listOf(
            "Giant pandas eat almost nothing but bamboo.",
            "Newborn pandas are tinier than a soda can!",
        ),
        "animal_pangolin" to listOf(
            "Pangolins are the only mammals covered in big scales.",
            "They curl into an armored ball when scared.",
        ),
        "animal_panther" to listOf(
            "\"Panther\" often means a black leopard.",
            "Melanistic leopards still have hidden spots!",
        ),
        "animal_pig" to listOf(
            "Pigs are clever and love cooling mud baths.",
            "Their sniffers find tasty roots underground.",
        ),
        "animal_polarbear" to listOf(
            "Polar bears have black skin under their white fur!",
            "Wide paws help them walk on snow and swim in icy seas.",
        ),
        "animal_porcupine" to listOf(
            "Porcupine quills are sharp hairs — not thrown like darts!",
            "Indian porcupines dig burrows in forests.",
        ),
        "animal_python" to listOf(
            "Pythons squeeze gently to catch dinner — no venom.",
            "Indian rock pythons can be very long.",
        ),
        "animal_rabbit" to listOf(
            "Rabbit teeth never stop growing!",
            "They thump back feet to warn of danger.",
        ),
        "animal_rat" to listOf(
            "Rats are clever problem-solvers.",
            "They can squeeze through surprisingly small holes.",
        ),
        "animal_rhino" to listOf(
            "A rhino's horn is made of keratin — like fingernails!",
            "One-horned rhinos live in Kaziranga, Assam.",
        ),
        "animal_seal" to listOf(
            "Seals clap and bark — some keep a beat!",
            "Thick blubber keeps them warm in cold seas.",
        ),
        "animal_sheep" to listOf(
            "Sheep remember faces of friends.",
            "One sheep can grow enough wool for a sweater.",
        ),
        "animal_snake" to listOf(
            "Snakes smell the world with their tongues!",
            "India has both gentle snakes and famous cobras.",
        ),
        "animal_squirrel" to listOf(
            "Squirrels fake-bury nuts to trick thieves!",
            "Indian palm squirrels have cute stripes.",
        ),
        "animal_tiger" to listOf(
            "Every tiger's stripe pattern is unique!",
            "The Bengal tiger is India's national animal.",
        ),
        "animal_tortoise" to listOf(
            "Tortoises can live longer than many humans!",
            "Star tortoises have pretty shell patterns.",
        ),
        "animal_turtle" to listOf(
            "A turtle's shell is part of its body!",
            "Olive ridley turtles nest on Indian beaches.",
        ),
        "animal_urchin" to listOf(
            "Sea urchins look like spiky little balls on the reef.",
            "They munch on algae and help keep the sea clean.",
        ),
        "animal_weasel" to listOf(
            "Weasels are skinny enough to chase mice into holes.",
            "They zip around with boundless energy.",
        ),
        "animal_whale" to listOf(
            "Blue whales are the biggest animals that ever lived!",
            "Whales sing underwater songs that travel far.",
        ),
        "animal_wolf" to listOf(
            "Wolves howl to keep the pack together.",
            "Indian wolves live in grasslands and scrub.",
        ),
        "animal_yak" to listOf(
            "Yaks wear thick coats for freezing mountains.",
            "People in the Himalayas use yak milk and wool.",
        ),


        "animal_zebra" to listOf(
            "Every zebra's stripe pattern is unique!",
            "Kids love their stripes at the zoo.",
        ),
        "bird_babbler" to listOf(
            "Babblers hop in noisy little gangs through bushes.",
            "Jungle babblers are common in Indian gardens.",
        ),
        "bird_barbet" to listOf(
            "Barbets have stout beaks for fruit and nesting holes.",
            "The coppersmith barbet's call sounds like a tiny hammer!",
        ),
        "bird_budgerigar" to listOf(
            "Budgies are tiny Australian parrots that love to chatter.",
            "Wild ones are green and yellow — pet colors can be almost any shade!",
        ),
        "bird_bulbul" to listOf(
            "Red-whiskered bulbuls sport a perky crest.",
            "Their cheerful calls fill many Indian mornings.",
        ),
        "bird_bustard" to listOf(
            "The great Indian bustard is a tall grassland bird.",
            "It needs wide open plains to thrive.",
        ),
        "bird_canary" to listOf(
            "Canaries sing bright cheerful songs.",
            "They were once taken into mines to warn of bad air.",
        ),
        "bird_cormorant" to listOf(
            "Cormorants dive underwater to catch fish.",
            "You often see them drying wings by Indian lakes.",
        ),
        "bird_crane" to listOf(
            "Cranes dance with fancy wing flaps.",
            "They have long legs for wading in shallow water.",
        ),
        "bird_crow" to listOf(
            "Crows are puzzle geniuses and remember faces.",
            "Indian house crows are city experts.",
        ),
        "bird_cuckoo" to listOf(
            "The Asian koel’s song means mango season to many kids!",
            "Some cuckoos lay eggs in other birds' nests.",
        ),
        "bird_dove" to listOf(
            "Doves coo softly to their partners.",
            "Spotted doves are gentle garden visitors.",
        ),
        "bird_drongo" to listOf(
            "Drongos are brave — they mob bigger birds!",
            "The black drongo has a forked tail like scissors.",
        ),
        "bird_duck" to listOf(
            "Duck feet work like paddles for swimming.",
            "They waterproof feathers with special oil.",
        ),
        "bird_eagle" to listOf(
            "Eagles spot prey from high in the sky.",
            "The majestic steppe eagle visits India in winter.",
        ),
        "bird_egret" to listOf(
            "Egrets stand still, then strike fast for fish.",
            "Cattle egrets walk beside cows to catch insects!",
        ),
        "bird_emperorpenguin" to listOf(
            "Emperor penguins huddle in circles to share heat.",
            "Dads balance eggs on their feet in icy winters.",
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
            "Colorful finches visit gardens and fields.",
        ),
        "bird_flamingo" to listOf(
            "Flamingos turn pink from the food they eat!",
            "Huge flocks visit Indian salt lakes in winter.",
        ),
        "bird_francolin" to listOf(
            "Francolins are plump birds of fields and scrub.",
            "Their loud calls ring out at dawn.",
        ),
        "bird_goose" to listOf(
            "Geese fly in a V shape to save energy.",
            "They honk to keep the flock together.",
        ),
        "bird_hawk" to listOf(
            "Hawks ride warm air without much flapping.",
            "Their eyesight is many times sharper than ours.",
        ),
        "bird_hen" to listOf(
            "Hens cluck to call chicks when they find food.",
            "An egg forms inside a hen in about a day.",
        ),
        "bird_heron" to listOf(
            "Herons spear fish with dagger beaks.",
            "They fold long necks into an S when flying.",
        ),
        "bird_hoopoe" to listOf(
            "Hoopoes have a fancy crown of feathers.",
            "They dig in lawns for tasty grubs.",
        ),
        "bird_hornbill" to listOf(
            "Hornbills have a helmet-like casque on their beaks.",
            "Great hornbills are treasures of Indian forests.",
        ),
        "bird_hummingbird" to listOf(
            "Hummingbirds can hover and even fly backward!",
            "Their wings beat so fast they hum like tiny engines.",
        ),
        "bird_ibis" to listOf(
            "Ibises dig snacks from mud with curved beaks.",
            "Black-headed ibises wade in Indian wetlands.",
        ),
        "bird_junglefowl" to listOf(
            "Red junglefowl are the wild ancestors of chickens!",
            "Roosters in villages share their looks.",
        ),
        "bird_kestrel" to listOf(
            "Kestrels hover in place watching for mice.",
            "They are small but mighty hunters.",
        ),
        "bird_kingfisher" to listOf(
            "Kingfishers dive headfirst to catch fish.",
            "The white-throated kingfisher is common in India.",
        ),
        "bird_kite" to listOf(
            "Black kites soar over Indian cities looking for scraps.",
            "They tilt and glide like living paper kites.",
        ),
        "bird_koel" to listOf(
            "The koel's ku-oo song is famous in Indian summers.",
            "Males are black; females are speckled brown.",
        ),
        "bird_lark" to listOf(
            "Larks sing while flying high in the sky.",
            "Their songs sparkle over open fields.",
        ),
        "bird_lovebird" to listOf(
            "Lovebirds often sit in close pairs — that's the name!",
            "They are colorful little parrots.",
        ),
        "bird_macaw" to listOf(
            "Macaws have huge colorful wings and strong nut-cracking beaks.",
            "They live in loud, friendly flocks in rainforests.",
        ),
        "bird_magpie" to listOf(
            "Magpies are noisy, clever members of the crow family.",
            "Some kinds flash beautiful blue wings.",
        ),
        "bird_munia" to listOf(
            "Munias are tiny seed-eating finches.",
            "Scaly-breasted munias visit grassy fields.",
        ),
        "bird_myna" to listOf(
            "Common mynas strut boldly in markets and parks.",
            "They can copy sounds and other birds.",
        ),
        "bird_nightingale" to listOf(
            "Nightingales sing beautiful songs, even at night.",
            "Poets have written about their music for centuries.",
        ),
        "bird_nightjar" to listOf(
            "Nightjars hunt moths at dusk with wide mouths.",
            "Their camouflage makes them look like bark.",
        ),

        "bird_nuthatch" to listOf(
            "Nuthatches walk headfirst down tree trunks!",
            "They jam nuts into bark and hammer them open.",
        ),
        "bird_oriole" to listOf(
            "Golden orioles flash bright yellow in trees.",
            "Their flute-like whistle is lovely.",
        ),
        "bird_osprey" to listOf(
            "Ospreys are fish-hunting specialists.",
            "They dive feet-first to grab a catch.",
        ),
        "bird_ostrich" to listOf(
            "Ostriches are the biggest birds and lay giant eggs.",
            "They outrun most horses over short distances.",
        ),
        "bird_owl" to listOf(
            "Owls twist their heads almost all the way around.",
            "Soft feathers make their flight whisper-quiet.",
        ),
        "bird_parakeet" to listOf(
            "Rose-ringed parakeets are bright green city birds in India.",
            "They screech happily from fruit trees.",
        ),
        "bird_parrot" to listOf(
            "Parrots copy human words and funny sounds.",
            "They climb with their beak like a third foot.",
        ),
        "bird_peacock" to listOf(
            "Peacocks fan huge colorful tails to impress friends.",
            "The Indian peafowl is India's national bird!",
        ),
        "bird_pelican" to listOf(
            "Pelicans scoop fish in huge pouchy beaks.",
            "Flocks visit Indian lakes in winter.",
        ),
        "bird_penguin" to listOf(
            "Penguins \"fly\" underwater with flipper-wings.",
            "Zoo penguins make kids giggle and cheer.",
        ),
        "bird_pheasant" to listOf(
            "Male pheasants wear shiny colorful feathers.",
            "They explode into noisy flight from grass.",
        ),
        "bird_pigeon" to listOf(
            "Pigeons find their way home from far away.",
            "They were once used to carry messages.",
        ),
        "bird_plover" to listOf(
            "Plovers nest on open ground and beaches.",
            "Parents pretend to be hurt to lead danger away!",
        ),
        "bird_quail" to listOf(
            "Quail chicks walk and find food soon after hatching.",
            "They make a funny whistling call.",
        ),
        "bird_raven" to listOf(
            "Ravens solve puzzles and plan ahead.",
            "They even slide on snow for fun!",
        ),
        "bird_robin" to listOf(
            "Indian robins flick their tails while hopping.",
            "They are bold little garden birds.",
        ),
        "bird_roller" to listOf(
            "Indian rollers flash bright blue wings in flight.",
            "They're sometimes called neelkanth.",
        ),
        "bird_rooster" to listOf(
            "Roosters crow to claim their yard — even before sunrise!",
            "They fluff up to protect hens.",
        ),
        "bird_sandpiper" to listOf(
            "Sandpipers probe wet sand with thin beaks.",
            "They scurry along beaches like wind-up toys.",
        ),
        "bird_saruscrane" to listOf(
            "Sarus cranes are the tallest flying birds on Earth!",
            "Pairs often stay together for life.",
        ),
        "bird_seagull" to listOf(
            "Seagulls drink both fresh and salt water.",
            "They're clever at snatching beach snacks!",
        ),
        "bird_sparrow" to listOf(
            "House sparrows chirp cheerful city songs.",
            "They take dust baths to clean feathers.",
        ),
        "bird_starling" to listOf(
            "Huge starling flocks swirl in sky dances.",
            "They mimic car alarms and other birds.",
        ),
        "bird_stork" to listOf(
            "Storks nest high and clap their beaks.",
            "Painted storks look like they wore bright makeup!",
        ),
        "bird_sunbird" to listOf(
            "Sunbirds sip nectar like tiny hummingbirds.",
            "Males often shine with metallic colors.",
        ),
        "bird_swan" to listOf(
            "Swans often swim in graceful pairs.",
            "Baby swans are called cygnets.",
        ),
        "bird_swift" to listOf(
            "Swifts spend most of their life flying!",
            "They catch insects high in the sky.",
        ),
        "bird_teal" to listOf(
            "Teals are small dabbling ducks.",
            "They tip upside-down to nibble pond plants.",
        ),
        "bird_tern" to listOf(
            "Terns dive-bomb fish with sharp beaks.",
            "Arctic terns migrate farther than almost any animal.",
        ),
        "bird_toucan" to listOf(
            "A toucan's giant beak is light and hollow inside.",
            "Bright colors help them spot flock friends in the trees!",
        ),
        "bird_turkey" to listOf(
            "Wild turkeys can fly short distances!",
            "They gobble and puff up for fancy shows.",
        ),
        "bird_umbrellabird" to listOf(
            "Umbrellabirds have a fluffy crest like a tiny umbrella!",
            "They live in rainforests and boom with deep calls.",
        ),
        "bird_vulture" to listOf(
            "Vultures clean nature by eating leftovers!",
            "They soar for hours searching for food.",
        ),
        "bird_wagtail" to listOf(
            "Wagtails bob their long tails up and down.",
            "They dash after insects on lawns.",
        ),
        "bird_weaver" to listOf(
            "Weaver birds stitch amazing hanging nests!",
            "Baya weavers build nests that look like grass baskets.",
        ),
        "bird_woodpecker" to listOf(
            "Woodpeckers drum on trees to find bugs.",
            "Special skull padding protects their brains.",
        ),
        "bird_wren" to listOf(
            "Tiny wrens sing surprisingly loud songs.",
            "They stuff nests into tiny nooks.",
        ),
        "bird_yellowhammer" to listOf(
            "Yellowhammers are bright yellow songbirds of fields.",
            "Their song sounds a bit like \"a little bit of bread and no cheese!\"",
        ),
        "city_addisababa" to listOf(
            "Addis Ababa means \"new flower\" and sits high in the mountains.",
            "It is home to African Union headquarters.",
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
            "Gardens by the Bay lights up giant \\",
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
        "flower_bluebell" to listOf(
            "Bluebell woods carpet the ground in blue each spring.",
            "Their bells nod on thin stems.",
        ),
        "flower_bougainvillea" to listOf(
            "Bougainvillea's bright \"petals\" are actually leaves!",
            "It covers Indian walls and gardens in hot color.",
        ),
        "flower_calendula" to listOf(
            "Calendula flowers look like little orange suns.",
            "They're also called pot marigold.",
        ),
        "flower_canna" to listOf(
            "Canna lilies hold bold flowers on tall stems.",
            "Their big leaves look tropical and grand.",
        ),
        "flower_carnation" to listOf(
            "Carnation petals have frilly zigzag edges.",
            "Different colors send different friendly messages.",
        ),
        "flower_champa" to listOf(
            "Champa flowers smell sweet in warm evenings.",
            "Frangipani/champa trees line many Indian streets.",
        ),
        "flower_chrysanthemum" to listOf(
            "Chrysanthemums are festive autumn blooms.",
            "In India they're also called shevanti.",
        ),
        "flower_crossandra" to listOf(
            "Crossandra is a bright orange garden favorite.",
            "It keeps flowering in warm weather.",
        ),
        "flower_cosmos" to listOf(
            "Cosmos flowers dance on tall thin stems.",
            "Butterflies love their open, sunny faces.",
        ),
        "flower_daffodil" to listOf(
            "Daffodils are cheerful spring trumpets of yellow.",
            "Plant the bulbs once and they return each year!",
        ),
        "flower_dahlia" to listOf(
            "Dahlias were first grown in Mexico.",
            "Blooms can be tiny pom-poms or dinner-plate huge!",
        ),
        "flower_daisy" to listOf(
            "Daisy centers are packed with tiny flowers!",
            "Kids love making daisy chains.",
        ),

        "flower_eveningprimrose" to listOf(
            "Evening primrose opens as daylight fades.",
            "Soft yellow blooms welcome night moths.",
        ),
        "flower_frangipani" to listOf(
            "Frangipani flowers smell like perfume factories.",
            "They're the same family as champa.",
        ),
        "flower_gardenia" to listOf(
            "Gardenias smell like creamy perfume.",
            "Their shiny leaves stay green year-round in warm places.",
        ),
        "flower_geranium" to listOf(
            "Garden geraniums love sunny windowsills.",
            "Some leaves smell like lemon, rose, or mint!",
        ),
        "flower_gulmohar" to listOf(
            "Gulmohar trees blaze fiery red in summer.",
            "They're also called flame trees.",
        ),
        "flower_hibiscus" to listOf(
            "Hibiscus flowers can be bigger than your hand.",
            "They're used in hair oil stories and garden hedges.",
        ),
        "flower_hyacinth" to listOf(
            "Hyacinths pack sweet scent from dense spikes.",
            "Bulbs can bloom indoors in winter.",
        ),
        "flower_iris" to listOf(
            "Iris flowers are named after the rainbow goddess.",
            "Their leaves grow like green swords.",
        ),
        "flower_ixora" to listOf(
            "Ixora makes tight clusters of tiny bright stars.",
            "It's a favorite hedge plant in warm India.",
        ),
        "flower_jasmine" to listOf(
            "Jasmine releases its strongest perfume at night.",
            "People weave it into gajra garlands for celebrations.",
        ),
        "flower_lavender" to listOf(
            "Lavender's smell helps many people feel calm.",
            "Bees go crazy for its purple spikes.",
        ),
        "flower_lily" to listOf(
            "Lilies grow from underground bulbs.",
            "Some kinds perfume a whole room.",
        ),
        "flower_lotus" to listOf(
            "Lotus flowers rise clean from muddy water.",
            "The lotus is India's national flower!",
        ),
        "flower_magnolia" to listOf(
            "Magnolias are ancient — they bloomed with dinosaurs!",
            "Fuzzy buds open into big waxy cups.",
        ),
        "flower_marigold" to listOf(
            "Marigolds fill Indian festivals with orange garlands.",
            "Their smell can help keep some garden bugs away.",
        ),
        "flower_mogra" to listOf(
            "Mogra is a beloved Indian jasmine.",
            "Its white buds scent warm nights and weddings.",
        ),
        "flower_morningglory" to listOf(
            "Morning glory blooms open with the sunrise.",
            "By afternoon many flowers gently close again.",
        ),
        "flower_narcissus" to listOf(
            "Narcissus is the fancy family name for daffodils.",
            "In myth, Narcissus stared at his reflection.",
        ),
        "flower_nasturtium" to listOf(
            "Nasturtium leaves taste peppery — edible flowers!",
            "They spill color over garden edges.",
        ),
        "flower_oleander" to listOf(
            "Oleander (kaner) grows bright pink or white blooms.",
            "It's pretty — and not for tasting.",
        ),
        "flower_nigella" to listOf(
            "Nigella is also called love-in-a-mist.",
            "Kalonji seeds of a cousin spice Indian breads.",
        ),
        "flower_orchid" to listOf(
            "Orchids are one of the biggest plant families.",
            "Vanilla flavor comes from an orchid seed pod!",
        ),
        "flower_pansy" to listOf(
            "Pansies look like little faces with soft velvet petals.",
            "They love cool weather and paint gardens with purple and yellow.",
        ),
        "flower_parijat" to listOf(
            "Parijat flowers fall like tiny white-orange stars at dawn.",
            "They're also called night-flowering jasmine.",
        ),
        "flower_periwinkle" to listOf(
            "Periwinkle (sadabahar) blooms almost all year!",
            "Bright pink and white stars carpet gardens.",
        ),
        "flower_petunia" to listOf(
            "Petunias pour color from pots all summer.",
            "They're cousins of tomatoes and potatoes!",
        ),
        "flower_poppy" to listOf(
            "Poppy petals are as thin as tissue paper.",
            "Some seed pods make tiny seeds on bread rolls.",
        ),
        "flower_rose" to listOf(
            "Roses have been garden favorites for thousands of years.",
            "Gulab jamun and rose water celebrate their scent.",
        ),
        "flower_sampige" to listOf(
            "Sampige (champak) flowers smell richly sweet.",
            "They're offered in South Indian temples.",
        ),
        "flower_snapdragon" to listOf(
            "Squeeze a snapdragon and the \"mouth\" opens!",
            "Seed pods look like tiny skulls — in a cute way.",
        ),
        "flower_sunflower" to listOf(
            "Young sunflowers turn to follow the sun!",
            "One head can hold hundreds of seeds.",
        ),
        "flower_tuberose" to listOf(
            "Tuberose (rajnigandha) smells strongest at night.",
            "Tall spikes of white blooms scent gardens.",
        ),
        "flower_tulip" to listOf(
            "Tulips came to gardens from Turkey and Central Asia.",
            "During \"tulip mania,\" rare bulbs cost more than houses!",
        ),
        "flower_violet" to listOf(
            "Violets often hide low in the grass.",
            "Some candy and syrups are violet-flavored.",
        ),
        "flower_yarrow" to listOf(
            "Yarrow has flat clusters of tiny flowers.",
            "Feathery leaves smell herby when crushed.",
        ),
        "flower_ylangylang" to listOf(
            "Ylang-ylang flowers are used in fancy perfumes.",
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
        "fruit_amla" to listOf(
            "Amla is packed with vitamin C — a tiny sour powerhouse!",
            "It is also called Indian gooseberry.",
        ),
        "fruit_apple" to listOf(
            "An apple floats because it's about 25% air!",
            "Cut one the right way and seeds make a star.",
        ),
        "fruit_apricot" to listOf(
            "Apricots are fuzzy cousins of peaches.",
            "Dried apricots make chewy sweet snacks.",
        ),
        "fruit_avocado" to listOf(
            "Avocados have one giant seed in the middle.",
            "They're creamy fruits mashed into many snacks.",
        ),
        "fruit_banana" to listOf(
            "Bananas are berries — but strawberries aren't!",
            "They're curved because they grow toward the sun.",
        ),
        "fruit_ber" to listOf(
            "Ber is a crunchy-sweet fruit of dry Indian lands.",
            "It is also called jujube or bordi.",
        ),
        "fruit_blackberry" to listOf(
            "Blackberries are clusters of tiny juice bubbles.",
            "Wild vines can be prickly — and delicious!",
        ),
        "fruit_blueberry" to listOf(
            "Blueberries wear a dusty white \"bloom\" that keeps them fresh.",
            "They start green and turn deep blue when ripe.",
        ),
        "fruit_cherry" to listOf(
            "One cherry tree can make thousands of cherries!",
            "Cherry pits hide inside like little stones.",
        ),
        "fruit_chickoo" to listOf(
            "Chickoo tastes like brown sugar and caramel.",
            "Many kids know it as chikoo or sapota.",
        ),
        "fruit_coconut" to listOf(
            "Coconuts can float across oceans to new islands!",
            "You can drink the water and eat the white meat.",
        ),
        "fruit_custardapple" to listOf(
            "Custard apple has creamy flesh like dessert.",
            "In India many call it sitaphal.",
        ),
        "fruit_date" to listOf(
            "Dates grow in huge bunches high on palm trees.",
            "They're naturally super sweet.",
        ),
        "fruit_dragonfruit" to listOf(
            "Dragonfruit grows on a climbing cactus!",
            "Tiny black seeds crunch in bright pink flesh.",
        ),
        "fruit_fig" to listOf(
            "A fig is like a flower that bloomed inside-out!",
            "Fresh figs are soft and honey-sweet.",
        ),
        "fruit_gooseberry" to listOf(
            "Gooseberries can be tart green or sweet pink jewels.",
            "They grow on prickly bushes.",
        ),
        "fruit_grape" to listOf(
            "Grapes grow in bunches on climbing vines.",
            "Dried grapes become raisins!",
        ),
        "fruit_guava" to listOf(
            "Guavas often have more vitamin C than oranges.",
            "You can eat the soft seeds with the fruit.",
        ),
        "fruit_jackfruit" to listOf(
            "Jackfruit is the world's biggest tree fruit!",
            "Its yellow bulbs taste sweet and tropical.",
        ),
        "fruit_jamun" to listOf(
            "Jamun juice can stain tongues purple!",
            "The trees fruit in hot Indian summers.",
        ),
        "fruit_kiwi" to listOf(
            "Kiwifruit hides bright green under fuzzy brown skin.",
            "It's named after New Zealand's kiwi bird.",
        ),
        "fruit_lemon" to listOf(
            "Lemon juice is so sour it makes faces scrunch!",
            "A squeeze brightens water, dal, and snacks.",
        ),
        "fruit_lime" to listOf(
            "Limes are tiny green citrus bombs of sour!",
            "Nimbu-paani is a classic Indian cooler.",
        ),
        "fruit_lychee" to listOf(
            "Lychees wear a bumpy pink peel you unzip.",
            "Inside is a juicy white ball that smells floral.",
        ),
        "fruit_mango" to listOf(
            "Mango is often called the king of fruits!",
            "India grows more mangoes than any other country.",
        ),
        "fruit_melon" to listOf(
            "Melons are mostly water — perfect for hot days!",
            "A ripe melon smells sweet at the stem end.",
        ),
        "fruit_mosambi" to listOf(
            "Mosambi is a sweet, low-sour citrus.",
            "Many kids drink fresh mosambi juice.",
        ),
        "fruit_mulberry" to listOf(
            "Mulberry juice stains fingers purple fast!",
            "Silkworms love mulberry leaves.",
        ),
        "fruit_nashi" to listOf(
            "Nashi are crisp Asian pears that crunch like apples.",
            "They're round and juicy.",
        ),
        "fruit_nectarine" to listOf(
            "Nectarines are peaches without the fuzzy jackets.",
            "One gene change makes the skin smooth!",
        ),

        "fruit_orange" to listOf(
            "Oranges are packed with vitamin C.",
            "The peel's oils make fingers smell citrusy.",
        ),
        "fruit_papaya" to listOf(
            "Papaya seeds look like peppercorns and are edible!",
            "The orange flesh is soft like sunny butter.",
        ),
        "fruit_peach" to listOf(
            "Peach fuzz helps protect the soft skin.",
            "A peach pit hides a seed inside.",
        ),
        "fruit_pear" to listOf(
            "Pears ripen from the inside out.",
            "Their shape is like a lightbulb.",
        ),
        "fruit_pineapple" to listOf(
            "Pineapples take almost two years to grow one fruit!",
            "Each \"eye\" outside was once a tiny flower.",
        ),
        "fruit_plum" to listOf(
            "Plums can be purple, red, yellow, or green.",
            "Dried plums are called prunes.",
        ),
        "fruit_pomegranate" to listOf(
            "Pomegranates hide hundreds of juicy ruby seeds.",
            "In India it's a festive favorite fruit.",
        ),
        "fruit_pomelo" to listOf(
            "Pomelos are giant grandparents of grapefruits.",
            "Thick peels hide mild sweet-tart segments.",
        ),
        "fruit_raspberry" to listOf(
            "Raspberries are hollow when picked — like tiny cups.",
            "Each bump is a juice-filled fruitlet.",
        ),
        "fruit_starfruit" to listOf(
            "Starfruit slices look like little yellow stars.",
            "The whole fruit is edible — skin and all.",
        ),
        "fruit_strawberry" to listOf(
            "Strawberry seeds grow on the outside!",
            "They're the only fruit with seeds on their skin like that.",
        ),
        "fruit_tamarind" to listOf(
            "Tamarind pods hide sticky sweet-sour pulp.",
            "It's the tangy secret in chutneys and imli candy.",
        ),
        "fruit_tangerine" to listOf(
            "Tangerines peel easier than big oranges.",
            "They're named after the city of Tangier.",
        ),
        "fruit_ugli" to listOf(
            "Ugli fruit is a tangy mix of grapefruit, orange, and tangerine.",
            "Its bumpy peel looks rough, but the inside is juicy!",
        ),
        "fruit_watermelon" to listOf(
            "Watermelons are over 90% water!",
            "Some are so big you need two hands.",
        ),
        "fruit_yuzu" to listOf(
            "Yuzu is a fragrant citrus with bumpy skin.",
            "Chefs love its zippy aroma.",
        ),
        "vegetable_ashgourd" to listOf(
            "Ash gourd has a pale powdery skin like ash.",
            "It becomes soft sweet petha candy in Agra!",
        ),
        "vegetable_beans" to listOf(
            "Beans are seeds that pack plant protein.",
            "Climbing beans twirl up poles like living spirals.",
        ),
        "vegetable_beetroot" to listOf(
            "Beetroot can turn tongues bright pink!",
            "It's sweet enough for salads and juices.",
        ),
        "vegetable_bittergourd" to listOf(
            "Bitter gourd is bumpy and famously bitter.",
            "Many Indian kitchens cook it with spices and jaggery.",
        ),
        "vegetable_bottlegourd" to listOf(
            "Bottle gourd (lauki) is light green and cooling.",
            "It grows long on climbing vines.",
        ),
        "vegetable_brinjal" to listOf(
            "Brinjal is the Indian name for eggplant.",
            "It soaks up spices in baingan dishes.",
        ),
        "vegetable_broccoli" to listOf(
            "Broccoli florets are tiny flower buds!",
            "It loves cooler weather.",
        ),
        "vegetable_cabbage" to listOf(
            "A cabbage is a giant bud of layered leaves.",
            "It becomes crunchy salads and cozy sabzi.",
        ),
        "vegetable_capsicum" to listOf(
            "Capsicums can be green, yellow, orange, or red.",
            "Red ones stayed longer and turned sweeter.",
        ),
        "vegetable_carrot" to listOf(
            "Carrots were purple and yellow before orange!",
            "Gajar ka halwa makes them into dessert.",
        ),
        "vegetable_cauliflower" to listOf(
            "Cauliflower is tight white flower buds.",
            "You can even find orange and purple kinds!",
        ),
        "vegetable_chickpea" to listOf(
            "Chickpeas become creamy hummus when blended.",
            "In India they're chana — from chole to snacks.",
        ),
        "vegetable_chili" to listOf(
            "Chili heat comes from a chemical called capsaicin.",
            "Birds don't feel the burn — so they spread seeds!",
        ),
        "vegetable_clusterbeans" to listOf(
            "Cluster beans (gavar) grow in neat bunches.",
            "They're a favorite green sabzi vegetable.",
        ),
        "vegetable_corn" to listOf(
            "Each corn silk strand connects to one kernel.",
            "Bhutta roasted on coals is a monsoon treat!",
        ),
        "vegetable_cucumber" to listOf(
            "Cucumbers are over 95% water!",
            "They're botanically fruits, but we cook them as veggies.",
        ),
        "vegetable_drumstick" to listOf(
            "Drumstick pods are long and green like sticks.",
            "Moringa leaves of the same tree are super healthy.",
        ),
        "vegetable_edamame" to listOf(
            "Edamame are young green soybeans in fuzzy pods.",
            "You squeeze the beans straight into your mouth.",
        ),
        "vegetable_elephantyam" to listOf(
            "Elephant yam (suran) is a giant starchy corm.",
            "It becomes spicy suran sabzi in many homes.",
        ),
        "vegetable_endive" to listOf(
            "Endive is grown pale for a mild crunch.",
            "It adds a gentle bitter bite to salads.",
        ),
        "vegetable_fenugreek" to listOf(
            "Fenugreek leaves are methi — slightly bitter and lovely.",
            "The seeds are a warm Indian spice too.",
        ),
        "vegetable_garlic" to listOf(
            "Garlic cloves are sections of an underground bulb.",
            "Crushing garlic releases its strong smell.",
        ),
        "vegetable_ginger" to listOf(
            "Ginger is a spicy underground stem.",
            "Adrak chai uses it to warm you up.",
        ),
        "vegetable_ivygourd" to listOf(
            "Ivy gourd is also called tindora or kundru.",
            "Tiny oval veggies perfect for quick stir-fries.",
        ),
        "vegetable_kale" to listOf(
            "Kale stays sweet even after a light frost.",
            "Its curly leaves are vitamin powerhouses.",
        ),
        "vegetable_knolkhol" to listOf(
            "Knol khol is a crunchy bulb like a tiny cabbage stem.",
            "It's also called kohlrabi.",
        ),
        "vegetable_lentil" to listOf(
            "Lentils cook fast in many colors.",
            "Dal is one of India's everyday comfort foods.",
        ),
        "vegetable_lettuce" to listOf(
            "Lettuce leaves are packed with water and crunch.",
            "Romaine and iceberg are different leaf styles.",
        ),
        "vegetable_lobia" to listOf(
            "Lobia are black-eyed peas with a tiny dark spot.",
            "They make hearty curries and salads.",
        ),
        "vegetable_mushroom" to listOf(
            "Mushrooms are fungi — not plants!",
            "The part we eat is just the \"fruit\" of an underground network.",
        ),
        "vegetable_okra" to listOf(
            "Okra (bhindi) gets pleasantly sticky when cooked.",
            "Flowers look like pretty hibiscus blooms.",
        ),
        "vegetable_onion" to listOf(
            "Onions make you cry with a tear-gas-like vapor!",
            "They're the start of almost every tadka.",
        ),
        "vegetable_peas" to listOf(
            "Peas grow in green pods you can pop open.",
            "Each pod holds a neat row of round seeds.",
        ),
        "vegetable_pointedgourd" to listOf(
            "Pointed gourd is parwal — a desi summer veggie.",
            "It's great stuffed or cooked in gravy.",
        ),
        "vegetable_potato" to listOf(
            "Potatoes are underground stems called tubers!",
            "Aloo is a star of Indian kitchens.",
        ),
        "vegetable_pumpkin" to listOf(
            "Giant pumpkins can weigh more than a small car!",
            "Kaddu sabzi and soup taste cozy and sweet.",
        ),
        "vegetable_radish" to listOf(
            "Radishes grow ready to eat in just weeks.",
            "Mooli paratha makes them famous!",
        ),
        "vegetable_ridgegourd" to listOf(
            "Ridge gourd (turai) has raised ridges on the skin.",
            "Peeled and cooked, it turns soft and mild.",
        ),
        "vegetable_shallot" to listOf(
            "Shallots grow in clusters and taste milder than onions.",
            "Chefs love them for delicate flavor.",
        ),
        "vegetable_snakegourd" to listOf(
            "Snake gourd can grow incredibly long and curly!",
            "It's a climbing veggie of Indian kitchens.",
        ),
        "vegetable_spinach" to listOf(
            "Spinach leaves are packed with iron and vitamins.",
            "Palak paneer makes spinach famous worldwide.",
        ),
        "vegetable_sweetpotato" to listOf(
            "Sweet potatoes are sweet even before dessert!",
            "Shakarkandi is a winter street snack.",
        ),
        "vegetable_taro" to listOf(
            "Taro (arbi) has starchy corms and big leaves.",
            "It must be cooked well before eating.",
        ),
        "vegetable_tinda" to listOf(
            "Tinda is a small round Indian gourd.",
            "It cooks into soft, comforting sabzi.",
        ),
        "vegetable_tomato" to listOf(
            "Tomatoes are juicy and technically fruits!",
            "They make chutneys, curries, and sandwiches sing.",
        ),
        "vegetable_turnip" to listOf(
            "Turnips have peppery white or purple roots.",
            "People eat both the root and leafy tops.",
        ),
        "vegetable_ube" to listOf(
            "Ube is a bright purple yam used in ice cream and cakes.",
            "It's popular in Filipino desserts and tastes gently sweet.",
        ),
        "vegetable_yam" to listOf(
            "Yams are starchy tropical tubers.",
            "They're different from the sweet potatoes people sometimes call yams.",
        ),
        "vegetable_yambean" to listOf(
            "Yam bean (sankalu) is crunchy and juicy raw!",
            "It's also known as jicama in some places.",
        ),
        "vegetable_yardlongbean" to listOf(
            "Yardlong beans can grow as long as a school ruler!",
            "They're perfect for stir-fries.",
        ),
        "vegetable_yuca" to listOf(
            "Yuca (cassava) makes fluffy fries and tapioca pearls.",
            "It must be cooked properly before eating.",
        ),
        "vegetable_zucchini" to listOf(
            "Zucchini can grow huge if you forget to pick them!",
            "Flowers of the plant are edible too.",
        )
    )
}
