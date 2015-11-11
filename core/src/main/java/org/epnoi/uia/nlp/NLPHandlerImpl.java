package org.epnoi.uia.nlp;

import gate.Document;
import org.epnoi.model.exceptions.EpnoiInitializationException;
import org.epnoi.model.exceptions.EpnoiResourceAccessException;
import org.epnoi.model.modules.Core;
import org.epnoi.model.modules.NLPHandler;
import org.epnoi.model.parameterization.ParametersModel;
import org.epnoi.nlp.NLPProcessor;
import org.epnoi.nlp.NLPProcessorsPool;
import org.epnoi.nlp.gate.GATEInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@Component
public class NLPHandlerImpl implements NLPHandler {
	@Autowired
	private Core core;
	@Autowired
	private ParametersModel parameters;

	private NLPProcessorsPool pool;

	private static final Logger logger = Logger.getLogger(NLPHandlerImpl.class.getName());

	// ----------------------------------------------------------------------------------------------------------

	@Override
	@PostConstruct
	public void init() throws EpnoiInitializationException {

		logger.info("Initializing the NLPHandler");
		if (parameters.getNlp() != null) {
			GATEInitializer gateInitializer = new GATEInitializer();
			gateInitializer.init(parameters);
			this.pool = new NLPProcessorsPool();
			this.pool.init(this.parameters);
		} else {
			logger.severe(
					"The NLPHandler was not initialized since no nlp element is defined in the uia configuration file");
		}

	}

	// ----------------------------------------------------------------------------------------------------------

	@Override
	public Document process(String content) throws EpnoiResourceAccessException {
		if (this.parameters.getNlp() != null) {
			NLPProcessor processor = null;

			try {
				processor = pool.borrowProcessor();
			} catch (EpnoiResourceAccessException e) {
				return null;
			}

			Document document = processor.process(content);
			pool.returnProcessor(processor);
			return document;
		} else {
			throw new EpnoiResourceAccessException(
					"The NLPHandler was not initialized since no nlp element is defined in the uia configuration file");
		}

	}

	// ----------------------------------------------------------------------------------------------------------
/*FOR TEST
	public static void main(String[] args) {
		Core core = CoreUtility.getUIACore();
		System.out.println(core.getParameters().getNlp());
		try {
			System.out.println("-------> "+core.getNLPHandler().process("                                                                       "));
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//_testComplexConcurrentNLPRequests(core);

		// _testConcurrentNLPAccess(core);

	}
*/
	// ----------------------------------------------------------------------------------------------------------

	private static void _testComplexConcurrentNLPRequests(Core core) {
		long time = System.currentTimeMillis();
		int NUMBER_OF_THREADS = 50;
		Thread[] threads = new Thread[NUMBER_OF_THREADS];
		for (int i = 0; i < NUMBER_OF_THREADS; i++) {
			threads[i] = new Thread(new TestThread(core, "Thread " + i,
					"Whales are a widely distributed and diverse group of fully aquatic marine mammals. Whales are a widely distributed and diverse group of fully aquatic marine mammals. Whales are a widely Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.Whales are a widely distributed and diverse group of fully aquatic marine mammals.They are an informal grouping within the order Cetacea, excluding dolphins and porpoises, so to zoologists the grouping is paraphyletic. The whales comprise the extant families Cetotheriidae (whose only living member is the pygmy right whale), Balaenopteridae (the rorquals), Balaenidae (right whales), Eschrichtiidae (the gray whale), Monodontidae (belugas and narwhals), Physeteridae (the sperm whale), Kogiidae (the dwarf and pygmy sperm whale), and Ziphiidae (the beaked whales). There are 40 extant species of whales. The two suborders of whales, Mysticeti and Odontoceti, are thought to have split up around 34 million years ago. Whales, dolphins and porpoises belong to the clade Cetartiodactyla with even-toed ungulates and their closest living relatives are the hippopotamuses, having diverged about 40 million years ago. Whales range in size from the 2.6 metres (8.5 ft) and 135 kilograms (298 lb) dwarf sperm whale to the 34 metres (112 ft) and 190 metric tons (210 short tons) blue whale, which is also the largest creature on earth. Several species exhibit sexual dimorphism, in that the females are larger than males. They have streamlined bodies and two limbs that are modified into flippers. Though not as flexible or agile as seals, whales can travel at up to 20 knots. Balaenopterids use their throat pleats to expand their mouth to take in gulps of water. Balaenids have heads that can make up 40% of their body mass to take in water. Odontocetes have conical teeth designed for catching fish or squid. Mysticetes have a well developed sense of smell, whereas odontocetes have well-developed hearing − their hearing, that is adapted for both air and water, is so well developed that some can survive even if they're blind. Some species are well adapted for diving to great depths. They have a layer of fat, or blubber, under the skin to keep warm in the cold water. Although whales are widespread, most species prefer the colder waters of the Northern and Southern Hemispheres, and migrate to the equator to give birth. Odontocetes feed largely on fish and squid; but a few, like the sperm whale, feed on large invertebrates, such as giant squid. Grey whales are specialized for feeding on bottom-dwelling mollusks. Male whales typically mate with multiple females every year, but females only mate every two to three years. Calves are typically born in the spring and summer months and females bear all the responsibility for raising them.",
					time));
			threads[i].start();
		}
	}

	// ----------------------------------------------------------------------------------------------------------

	private static void _testSimpleConcurrentNLPRequests(Core core) {
		long time = System.currentTimeMillis();

		Thread threadA = new Thread(new TestThread(core, "ThreadA",
				"The meat, blubber and baleen of whales have traditionally been used by indigenous peoples of the Arctic. Whales have been depicted in various cultures worldwide, notably, the Inuit and the coastal peoples of Vietnam and Ghana; they sometimes hold whale funerals. Small whales, such as belugas, are commonly kept in captivity and are even sometimes trained to perform tricks. Once relentlessly hunted for their products, whales are now protected by international law.",
				time));
		threadA.start();

		Thread threadB = new Thread(new TestThread(core, "ThreadB",
				"Odontocetes are also known as toothed whales due to the presence of teeth as opposed to their counterparts, the mysticetes, and have only one blowhole. These animals rely on their well-developed sonar to find their way in the water. For locomotion, toothed whales send out ultra-sonic clicks using their melon, which then in turn bounce back at the whale.",
				time));
		threadB.start();

		Thread threadC = new Thread(new TestThread(core, "ThreadC",
				"Monodontids consist of two species: the beluga and the narwhal. They both reside in the frigid arctic and, likewise, both have large amounts of blubber. Belugas, being white, hunt in large pods near the surface and around pack ice, their coloration acting as camouflage. Narwhals, being black, hunt in large pods in the aphotic zone, but their underbelly still remains white to remain camouflaged when something is looking directly up or down at them. They have no dorsal fin to prevent collision with pack ice",
				time));
		threadC.start();

		try {
			threadA.join();
			threadB.join();
			threadC.join();
		} catch (InterruptedException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// ----------------------------------------------------------------------------------------------------------

class TestThread implements Runnable {
	private final String name;
	private final String sentence;
	private final Core core;
	private final long time;

	TestThread(Core core, String name, String sentence, long time) {
		this.name = name;
		this.sentence = sentence;
		this.core = core;
		this.time = time;
	}

	@Override
	public void run() {
		Document document=null;
		try {
			document = this.core.getNLPHandler().process(this.sentence);
		} catch (EpnoiResourceAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out
				.println("[" + name + "]>" + document.getAnnotations().size());
		System.out.println("It took " + (time - System.currentTimeMillis() + " !!!!!!"));

	}
}
