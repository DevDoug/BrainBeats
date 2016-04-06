package Views.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.brainbeats.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WikiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WikiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WikiFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ListView navList;
    public TextView mHeaderText;
    public TextView mContentText;


    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment WikiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WikiFragment newInstance() {
        WikiFragment fragment = new WikiFragment();
/*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    public WikiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_wiki, container, false);
        navList = (ListView) v.findViewById(R.id.WikiSideNav);
        mHeaderText = (TextView) v.findViewById(R.id.content_header);
        mContentText =(TextView) v.findViewById(R.id.content_text);
        navList.setAdapter(new ArrayAdapter<String>(
                this.getActivity(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1,
                new String[]{
                        "Definition",
                        "General Use",
                        "History",
                        "Current Theories",
                        "Methods",
                        "Selection",
                        "Cognition",
                        "Stress and Anxiety",
                        "Pain",
                        "Headaches",
                        "Mood",
                        "Behavior",
                        "Analysis",
                        "Conclusions",
                        "References"
                }));
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        mHeaderText.setText("Definition");
                        mContentText.setText("The term brainwave entrainment refers to the use of rhythmic stimuli with the intention of producing a frequency-following response of brainwaves to match the frequency of the stimuli. The stimulus is usually either visual (fl ashing lights) or auditory (pulsating tones). By those in the industry, it is also commonly called brain entrainment, audiovisual entrainment (AVE), audiovisual stimulation (AVS), auditory entrainment, or photic stimulation.");
                        break;
                    case 1:
                        mHeaderText.setText("General Use");
                        mContentText.setText("BWE is provided to the user in the form of a device that" +
                                "often includes goggles with light-emitting diodes (LEDs) and/or" +
                                "a CD, usually requiring the use of headphones. It also comes in" +
                                "the form of software, which allows the user more fl exibility in" +
                                "adjusting individual sessions to his/her needs. Sessions most" +
                                "commonly last from 20 to 60 minutes, during which a user sits" +
                                "either with his or her eyes closed in a quiet setting or, depending" +
                                "on the goals of the user and session used, with eyes open while" +
                                "A COMPREHENSIVE REVIEW OF THE PSYCHOLOGICAL" +
                                "EFFECTS OF BRAINWAVE ENTRAINMENT" +
                                "Tina L. Huang, PhD; Christine Charyton, PhD" +
                                "review article" +
                                "Objective  Brainwave entrainment (BWE), which uses rhythmic" +
                                "stimuli to alter brainwave frequency and thus brain states, has been" +
                                "investigated and used since the late 1800s, yet many clinicians and" +
                                "scientists are unaware of its existence. We aim to raise awareness" +
                                "and discuss its potential by presenting a systematic review of the" +
                                "literature from peer-reviewed journals on the psychological effects" +
                                "of BWE." +
                                "Data Sources  Terms used to describe BWE and psychological" +
                                "outcomes were used to search English language studies from OVID" +
                                "Medline (1950-2007), PsychInfo (1806-2007), and Scopus." +
                                "Study Selection  Twenty studies selected satisfi ed the following criterion:" +
                                "studies needed to use rhythmic stimuli with the aim of affecting" +
                                "psychological outcomes. Peer-reviewed experimental and quasiexperimental" +
                                "studies were accepted. Case studies and review articles" +
                                "were excluded. Psychological outcomes were measured using standard" +
                                "assessment methods or as deemed appropriate by peer review." +
                                "Data Extraction  Other clinical measurements, including electroencephalogram" +
                                "response, galvanic skin response, and neurotransmitter" +
                                "levels were not included." +
                                "Data Synthesis  Psychological outcomes addressed cognition," +
                                "stress and anxiety, pain relief, headaches or migraines, mood," +
                                "behavior, and premenstrual syndrome (PMS). Protocols included" +
                                "the use of single, alternating, ascending, or descending frequencies" +
                                "or were determined by the subject, using auditory and/or photic" +
                                "stimulation. Studies examined single session effects and/or longerterm" +
                                "multiple session effects." +
                                "Conclusions  Findings to date suggest that BWE is an effective" +
                                "therapeutic tool. People suffering from cognitive functioning defi -" +
                                "cits, stress, pain, headache/migraines, PMS, and behavioral problems" +
                                "benefi ted from BWE. However, more controlled trials are" +
                                "needed to test additional protocols with outcomes. (Altern Ther" +
                                "Health Med. 2008;14(5):38-49.)" +
                                "This article is protected by copyright. To share or copy this article, please click here or visit copyright.com. Use ISSN#10786791. To subscribe, click here or visit alternative-therapies.com." +
                                "Psychological Effects of Brainwave Entrainment ALTERNATIVE THERAPIES, sep/oct 2008, VOL. 14, NO. 5 39" +
                                "working. Those with a history of epilepsy are advised against use" +
                                "of photic stimulation." +
                                "The most commonly used methods of BWE are to stimulate" +
                                "the brain at the desired frequency via auditory tones, flashing" +
                                "lights, or a combination of both. The 3 most common forms of" +
                                "auditory stimulation use isochronic, monaural, or binaural beats." +
                                "Isochronic tones are evenly spaced tones that simply turn on and" +
                                "off. Monaural and binaural beats are presented as 2 tones with" +
                                "very similar frequencies, and the brain perceives a beat that is the" +
                                "difference between the 2 pitches. The pitches are presented together" +
                                "with monaural beats but fed separately to each ear with binaural" +
                                "beats. For photic stimulation, most devices use goggles with lights" +
                                "or a fl ashing screen, and most instructions suggest that the users" +
                                "eyes remained closed. Pulses of light can be presented as different" +
                                "waveforms or colors. Photic stimulation also can be presented" +
                                "independently to each eye or each visual fi eld in order to more" +
                                "effectively target stimulation to the right or left hemisphere. ");
                        break;
                    case 2:
                        mHeaderText.setText("History");
                        mContentText.setText("The first known clinical application of BWE was discovered" +
                                "by a French psychologist, Pierre Janet, in the late 1800s. Janet" +
                                "noted that his patients appeared calmer after being exposed to a" +
                                "rotating strobe wheel that was illuminated by a lantern, and thus" +
                                "he used this method therapeutically as needed.2" +
                                " After Berger" +
                                "showed that electrical activity could be recorded from the human" +
                                "brain in 1929,3" +
                                " Adrian and Mathews (1934) showed that the" +
                                "Berger rhythm (alpha) could be further amplifi ed by photic stimulation" +
                                "at the same frequency.4" +
                                " In 1942, Dempsey and Morison" +
                                "found that BWE could also be induced by a tactile stimulus,5" +
                                " and" +
                                "Chaitran reported entrainment effects with an auditory stimulus" +
                                "in 1959.6" +
                                " Psychological effects of BWE were further explored in" +
                                "1946 when fl ickering light produced frequency-dependent sensations" +
                                "of pattern, movement and color.7" +
                                " In 1959, BWE was" +
                                "found to reduce the need for anesthesia during surgery,8" +
                                " and in" +
                                "1975, it was found to enhance meditation.9" +
                                " The development of" +
                                "BWE tools proliferated after Osters 1973 article on the properties" +
                                "of the binaural beat.10 Research on the effects of BWE on" +
                                "pain, headaches, migraines, anxiety, and stress followed in the" +
                                "1980s and expanded in the 1990s to include learning and memory," +
                                "ADHD, learning disabilities, behavioral problems, and PMS");
                        break;
                    case 3:
                        mHeaderText.setText("Current Theories");
                        mContentText.setText("Placement of sensors on the scalp allows the measurement of" +
                                "brainwave patterns that refl ect the current state of an individual." +
                                "The best studied brainwave frequencies range from the slower" +
                                "delta frequencies (1-4 Hz), which are associated with deep sleep;" +
                                "to theta frequencies (4-8 Hz), which are associated with light" +
                                "sleep, creativity, and insight; to alpha frequencies (8-12 Hz), which" +
                                "reflect a calm and peaceful yet alert state; to beta frequencies" +
                                "(13-21 Hz), which are associated with a thinking, focusing state;" +
                                "to high beta frequencies (20-32 Hz), which are associated with" +
                                "intensity or anxiety.11 Research shows that presentation of a consistent" +
                                "rhythmic stimulus (usually either a pulsating light or a" +
                                "tone) within 8 to 10 Hz causes brainwaves in the occipital lobe," +
                                "parietal lobe, or temporal cortex to exhibit a frequency-following" +
                                "response that either resonates with the presenting stimulus or" +
                                "shows a frequency harmonic or a sub-harmonic of a stimulus.7,12-16" +
                                "Although many believe that the same mechanism applies to other" +
                                "frequencies, this has been harder to substantiate. Recent research" +
                                "suggests that baseline electroencephalogram (EEG)17 or emotional" +
                                "lability18 may influence post-stimulus EEG changes. Most" +
                                "researchers agree that emotional or cognitive changes do correlate" +
                                "with changes in the EEG but how or whether the EEG changes is" +
                                "likely to be dependent on the individuals current state. ");
                        break;
                    case 4:
                        mHeaderText.setText("Methods");
                        mContentText.setText("Ovid Medline databases (Ovid Medline [1950-2007], in process," +
                                "and other non-indexed citations), PsychInfo (1806-2007)," +
                                "and Scopus (1900-2007) were used. All papers containing any of" +
                                "the following terms were selected: visual entrainment, auditory" +
                                "evoked potentials, auditory entrainment, brain entrainment, brainwave" +
                                "entrainment, brain stimulation, brainwave stimulation, frequency" +
                                "following response, photic stimulation, photo stimulation," +
                                "photic driving, audio-visual entrainment, AVE, sonic entrainment," +
                                "evoked potentials, fl icker, brain AND entrainment, cortical evoked" +
                                "response, visual evoked response, afferent sensory stimulation, variable" +
                                "frequency photo-stimulation, repetitive sensory response, brain" +
                                "wave synchronizers, brainwave synchronizers, audiovisual stimulation," +
                                "AVS, auditory stimulation, binaural beats, monaural beats, isochronic" +
                                "beats, or isochronic tones." +
                                "To select psychological terms, we used information gathered" +
                                "from professional conferences, review articles, a review of websites" +
                                "of several of the most well-known BWE companies, and 2" +
                                "unpublished manuscripts by Dave Siever (The rediscovery of audiovisual" +
                                "entrainment technology [2000] and The physiology and applications" +
                                "of audio-visual entrainment technology [2006]). Articles" +
                                "containing the following clinical terms were selected: learning disorders," +
                                "learning disabilities, dementia, cognitive decline, Alzheimers," +
                                "intelligence, IQ, mental disorders, behavioral disorders, attention defi -" +
                                "cit, verbal learning, memory, creativity, depression, anger, rage," +
                                "migraine, headache, pain, anxiety, stress, premenstrual syndrome, or" +
                                "sleep. We then combined the outcomes of the BWE and the psychological" +
                                "searches. For each relevant original and review article" +
                                "found, references were examined for additional papers.");
                        break;
                    case 5:
                        mHeaderText.setText("Selection");
                        mContentText.setText("To be included in the review, articles had to examine the" +
                                "effects of BWE using auditory or visual stimulation on psychological" +
                                "outcomes. The stimuli had to be delivered using either" +
                                "pulses of lights or tones at frequencies hypothesized to have a" +
                                "benefi cial effect or a protocol based on a systematic approach" +
                                "toward addressing clinical outcomes. Only original full-length" +
                                "journal articles in peer-reviewed journals in English were included." +
                                "Case studies and review articles were excluded. Studies had to" +
                                "be of an experimental design using a comparison group or a pretest" +
                                "post-test design. Clinical or psychological outcomes had to " +
                                "40 ALTERNATIVE THERAPIES, sep/oct 2008, VOL. 14, NO. 5 Psychological Effects of Brainwave Entrainment" +
                                "be measured using reliable and appropriate test procedures as" +
                                "deemed by peer review. Selected studies were required to reveal" +
                                "statistical outcomes, such as descriptive statistics, analysis, and P" +
                                "values. The Figure shows the search strategy and number of articles" +
                                "retrieved for each step with each of the 4 databases." +
                                "RESULTS" +
                                "Overview" +
                                "Twenty articles met our criteria, and all examined 1 or more" +
                                "outcomes. We categorized psychological outcomes into the following" +
                                "categories: cognition (verbal outcomes [2], nonverbal/" +
                                "performance [2], attention [5], memory [3], and overall intelligence" +
                                "and achievement [2]), stress (short-term [5] and long-term" +
                                "stress/burnout [4]), pain (3), headaches/migraines (4), mood" +
                                "(3), behavioral problems (1), and PMS (1). These categories are" +
                                "divided into Tables 1-5." +
                                "Nine studies used healthy subjects, 4 used subjects with" +
                                "either learning disabilities or attention defi cit hyperactivity disorder" +
                                "(ADHD), 3 examined subjects with migraines, 2 used" +
                                "stressed subjects, 1 examined subjects with anxiety symptoms, 3" +
                                "examined subjects while experiencing day surgery, and 1 included" +
                                "subjects with bruxism or myofascial pain dysfunction syndrome." +
                                "Fifteen studies were of adults, 3 of children, 1 of college" +
                                "students, and 1 of elderly subjects. Studies that used children," +
                                "college students, and the elderly were primarily interested in cognitive" +
                                "functioning and academic outcomes. Nine of the studies" +
                                "used multiple sessions, ranging from approximately 7 to 100," +
                                "and another 9 studies used single sessions. Two additional studies" +
                                "used multiple sessions but also measured changes before and" +
                                "after each session. Session lengths ranged from 0.5 seconds to 60" +
                                "minutes. Frequency of sessions in long-term studies ranged from" +
                                "1 session per week to 2 sessions per day. Two studies used a protocol" +
                                "with alternating frequencies, 11 used constant frequencies" +
                                "(3 that were selected by the patient), 2 used 0.5-second frequency" +
                                "bursts, 3 studies began with descending frequencies and then" +
                                "ended in a single frequency, 1 study alternated between ascending" +
                                "and descending frequencies, and 1 study used 3 different" +
                                "tapes of theta and delta but gave no further details. Among studies" +
                                "that used constant or alternating frequencies as specifi ed by" +
                                "the investigators, frequencies ranged from low delta to high" +
                                "gamma. Nine studies used photic stimulation only, 6 used auditory" +
                                "stimulation, 4 used AVE, and 1 compared AVE to photic" +
                                "stimulation. The number of subjects in each study ranged from 4" +
                                "to 108. Seven groups had fewer than 20 subjects, 10 groups had" +
                                "between 21 and 40, and 3 groups had more than 40. Thirteen" +
                                "studies had control groups. ");
                        break;
                    case 6:
                        mHeaderText.setText("Cognition");
                        mContentText.setText("Within the 8 studies that addressed cognitive functioning, a" +
                                "large range of outcomes was examined, which we categorized" +
                                "into verbal skills, nonverbal skills, memory, attention, general" +
                                "intelligence, or success in school as measured by grade point" +
                                "average (GPA). Four of the 8 studies examined longer-term" +
                                "changes over weeks and multiple sessions, and 4 examined" +
                                "immediate effects of BWE." +
                                "One study that examined verbal abilities in school-aged children" +
                                "with ADHDused a protocol that alternated between alpha" +
                                "(10 Hz) and beta (15-18 Hz) AVE.19 A separate study used 4" +
                                "healthy adult subjects and compared the effects of theta (7 Hz)" +
                                "auditory stimulation with rain sounds to rain sounds alone to" +
                                "examine the same outcome (Table 1).20 The study that alternated" +
                                "between alpha and beta entrainment on children with ADHD" +
                                "found signifi cant increases with a standardized reading test, 19" +
                                "but the study that used only a single theta stimulation with" +
                                "healthy adults saw no signifi cant improvements.20" +
                                "Nonverbal skills (Table 1) were examined in 2 studies of" +
                                "children with LD or ADHD.21,22 There was no change with the" +
                                "Raven Progressive Matrices with 12 to 14 Hz photic stimulation.21" +
                                "However, in a separate study, 30 children were alternately" +
                                "exposed to an excitatory program (starting at 14 Hz and increasing" +
                                "to 40 Hz) and an inhibitory program (which started at 40 Hz" +
                                "and decreased to 14 Hz) over 6 weeks.22 These children showed a" +
                                "signifi cant improvement with arithmetic using WISC-III, suggesting" +
                                "that incorporation of gamma frequencies (38-42 Hz)" +
                                "should be examined further as a potential method to improve" +
                                "nonverbal skills such as math Five studies used 3 different stimulation methods (photic, auditory" +
                                "with binaural beats, and AVE) to examine attention (Table 1)." +
                                "Three studies19,21,22 examined children with ADHD or learning disabilities" +
                                "over multiple sessions; 2 studies examined normal adults over a" +
                                "single session.20,23 Four studies that used a protocol that involved beta" +
                                "stimulation found signifi cant improvements in attention using the" +
                                "Tests of Variable Attention (TOVA)20,21 or the WISC,21,22 but the study" +
                                "that used theta stimulation found no improvements.20" +
                                "Two studies that examined memory in different populations" +
                                "found signifi cant effects, but 1 study found a negative effect (Table" +
                                "1). Two single session studies examined the optimal photic frequency" +
                                "stimulation associated with trigram recognition in cognitively" +
                                "healthy middle-aged adults and seniors. Both studies" +
                                "concluded that the most trigrams were recognized with 10- or" +
                                "10.2-Hz stimulation.16,24 In a separate study, a single 30-minute session" +
                                "of theta stimulation in healthy adults reduced immediate" +
                                "recall using the Rey Auditory Verbal List Test compared to sessions" +
                                "with no BWE.20" +
                                "Two studies used photic stimulation with25 or without electrodermal" +
                                "stimulation (EDR)21 at different frequencies to examine its" +
                                "effect on general intelligence or GPA (Table 1). The studies selected" +
                                "children with ADHD21 or college students with academic challenges25" +
                                "and found that stimulation of 14 Hz alternating with 22" +
                                "Hz25 or 12 to 14 Hz21 over multiple sessions resulted in signifi cant" +
                                "improvements on GPA25 or the Scholastic Achievement Wechsler" +
                                "Individual Achievement Test.21" );
                        break;
                    case 7:
                        mHeaderText.setText("Stress And Anxiety");
                        mContentText.setText(" short-term stress relief and long-term stress/burnout (Table 2)." +
                                "Of the 5 studies that examined short-term stress (Table 2), 3 used" +
                                "auditory stimulation,26,27 1 study used AVE,28 and a fi fth compared" +
                                "photic stimulation to AVE.29 Two studies had subjects who were" +
                                "undergoing stressful medical procedures, 1 study had employees of" +
                                "an addiction care facility, another treated mildly anxious adults," +
                                "and the fi fth had healthy adults. Of the 2 groups that stimulated at" +
                                "alpha frequencies (10 Hz), 1 found a trend but not a signifi cant difference" +
                                "between the experimental groups (photic stimulation and" +
                                "AVE) and the control group.29 The other study, which compared" +
                                "alpha to beta stimulation, found a signifi cant difference after stimulation" +
                                "but not between the 2 frequencies.28 A study that used a combination" +
                                "of theta and delta frequencies and another study that used" +
                                "progressive slowing from alpha to 10 minutes of delta found signifi -" +
                                "cant reductions in anxiety.26,27 The smaller study that used just theta" +
                                "stimulation on healthy adults found no signifi cant differences in" +
                                "effects between stimulation and control conditions.20" +
                                "Of the 4 studies that examined long-term stress, 2 used AVE," +
                                "and 2 used auditory stimulation with binaural beats over 4 to 8" +
                                "weeks of sessions (Table 2). Two studies treated people with stressful" +
                                "occupations, 1 treated mildly anxious adults, and another used" +
                                "healthy adults. One study found no effect on state or trait anxiety in" +
                                "the Spielbergers State-Trait Anxiety Inventory (STAI) with theta and" +
                                "delta,26 and another found no effect with state anxiety with the STAI" +
                                "or tension/anxiety with the Profi le of Mood States (POMS) but did" +
                                "fi nd a difference with trait anxiety on the STAI with mostly delta" +
                                "stimulation.30 A third study that compared alpha to beta stimulation" +
                                "found significant effects in personal competence with alpha and" +
                                "emotional exhaustion with beta but not other measurements using" +
                                "the Maslachs Burnout Inventory (MBI).28 The study that had the" +
                                "most success began at 30 Hz and ramped the frequency down until" +
                                "the subject was relaxed for 15 minutes and then administered 8 to 14" +
                                "Hz for 7 minutes and found benefi cial effects with 75% of the outcomes" +
                                "(POMS, STAI, Observer Rating Inventory [ORI] and the" +
                                "Stanford Stress Questionnaire [SSQ]) used to measure long-term" +
                                "stress.31 Among these studies, there was no benefi t for subjects experiencing" +
                                "more stress or anxiety than for the healthy adults." +
                                "Pain" +
                                "The 2 studies that examined the effects of either photic or auditory" +
                                "stimulation on pain showed benefi cial effects with BWE (Table" +
                                "3).32,33 In a study of 40 patients ready to undergo their second esophagogastroduodenoscopy," +
                                "20 patients who received 9 Hz of photic" +
                                "stimulation during surgery were compared to 20 patients whose" +
                                "goggles were turned off. The experimental group had lower pain" +
                                "scores than controls, and 18 out of 20 who received photic stimulation" +
                                "experienced considerably less pain in comparison to their previous" +
                                "esophagogastroduodenoscopy.32 Another study used subjects" +
                                "with bruxism and myofasical pain dysfunction syndrome who were" +
                                "given isochronic tones of constant frequency and duration that were" +
                                "adjusted and selected by the patient and electromyographic (EMG)" +
                                "feedback. Subjects experienced signifi cantly less temporal mandibular" +
                                "joint pain and muscle spasms at the end of the 3-week period.33");
                        break;
                    case 8:
                        mHeaderText.setText("Headaches and Migraines");
                        mContentText.setText("Of the 3 studies that examined the effects of entrainment on" +
                                "migraines or headaches, 1 tested the ability of photic stimulation to" +
                                "prevent migraines, and the other 2 used photic stimulation as a treatment" +
                                "(Table 4). In a study that treated subjects with frequent" +
                                "migraines at 30 Hz over 30 days, 44% of subjects and 53% of those" +
                                "who normally had preceding warning signs had a decreased frequency" +
                                "of migraines.34 A separate study stimulated subjects with" +
                                "sinusitis or acute, chronic, or migraine headaches with 1 to 3 Hz of" +
                                "photic stimulation for 5 minutes. Most people with acute (14 out of" +
                                "15) and chronic (5 out of 6) headaches experienced complete relief," +
                                "but those with sinusitis and migraine headaches had no relief.35" +
                                "Closer testing of 4 patients with chronic muscle contraction" +
                                "headaches, using a variety of more stringent controls, confi rmed" +
                                "these fi ndings.35 In a third study, 7 subjects with migraines were" +
                                "given red LED goggles and allowed to choose a frequency from 0.5 to" +
                                "50 Hz, which they used for 5 to 60 minutes upon the occurrence of" +
                                "migraines. Forty-nine of 50 migraine headaches were relieved, 36" +
                                "were completely stopped, and the median duration of migraines" +
                                "decreased from 6 hours to 35 minutes.");
                        break;
                    case 9:
                        mHeaderText.setText("Mood");
                        mContentText.setText("Three studies examined mood with auditory stimulation using binaural beats in healthy adults. One study compared beta to theta/" + "delta stimulation over a single session and found that as measured by the POMS, confusion/bewilderment and fatigue/inertia increased\n" +
                                "and vigor/activity decreased with both session types, and depression/\n" +
                                "dejection increased with theta/delta but decreased with beta.23 A second\n" +
                                "study used 1-hour sessions that began at 10 Hz and progressively\n" +
                                "dropped to 2.5 Hz for 60 days and found no effect with depression.30\n" +
                                "In a small double-blind cross-over study, 4 adults were stimulated at 7\n" +
                                "Hz for a single session, and as measured by the POMS, total mood\n" +
                                "disturbance was not affected, but there was an increase in depression\n" +
                                "score.20 None of the protocols used in these 3 studies was specifi cally\n" +
                                "designed or hypothesized to ameliorate depression ");
                        break;
                    case 10:
                        mHeaderText.setText("Behavior");
                        mContentText.setText("One study tested the ability of photic stimulationto positively\n" +
                                "infl uence behavior in school-aged children with ADHD over multiple\n" +
                                "sessions. 21 It used the Achenbach Child Behavior Checklist to\n" +
                                "measure both the parental and child assessment of behavioral\n" +
                                "change before and after 15 sessions of photic stimulation at 12 to 14\n" +
                                "Hz, which was gradually withdrawn in cases vs controls. Both the\n" +
                                "parental assessment and the childs self-assessment of the childs\n" +
                                "behavior improved by approximately 70%");
                        break;
                    case 11:
                        mHeaderText.setText("Analysis");
                        mContentText.setText("To determine whether specifi c outcomes were associated with\n" +
                                "specifi c frequencies, we also grouped studies by frequency or pattern\n" +
                                "of frequency stimulation. Out of 4 outcomes that were examined\n" +
                                "with delta stimulation, only headaches/migraines35 and\n" +
                                "short-term stress27 improved but not long-term stress26,30 or\n" +
                                "mood.23,30 Theta was examined in conjunction with 2 outcomestudy\n" +
                                "groups and yet showed no benefi t for cognitive functioning,20\n" +
                                "mood,20 or relieving stress.26 A single session of alpha stimulation\n" +
                                "relieved stress for the employees of the addiction care facility28 but\n" +
                                "not for subjects undergoing a root canal,29 suggesting that the effectiveness\n" +
                                "of alpha could be based on the type of stress exposure.\n" +
                                "Alpha also appeared to relieve pain32 and improve personal competence\n" +
                                "using the MBI.28 Trigram recognition was also most effective\n" +
                                "at alpha in 2 separate adult populations.16,24 Beta appeared to\n" +
                                "improve attention,21,23 overall intelligence,21,25 short-term stress,28\n" +
                                "headaches,34 and behavioral problems21 and to relieve emotional\n" +
                                "exhaustion.28 It had no effect on nonverbal intelligence21 and\n" +
                                "mood.23 The alpha/beta protocol improved verbal skills and\n" +
                                "helped with attention,19 and in another study that used beta and\n" +
                                "gamma stimulation, it showed benefi cial effects with arithmetic\n" +
                                "skills in children with learning disabilities and/or ADHD theta stimulation hypothesized a benefi cial effect of beta stimulation\n" +
                                "on vigilance, but no hypothesis was made with regard to\n" +
                                "mood.23 Of the studies with specifi c hypotheses, there were positive\n" +
                                "findings in the 1 group that examined verbal skills, 4 out of 4\n" +
                                "groups with attention, 2 out of 2 with memory, 2 out of 2 with\n" +
                                "overall intelligence and achievement, 3 out of 3 with pain, 3 out of\n" +
                                "3 with migraines, 1 out of 1 with PMS, with the 1 study that examined\n" +
                                "behavioral problems, and the 2 studies that used theta/delta\n" +
                                "stimulation out of the 4 that examined short-term stress. Although\n" +
                                "beta stimulation was not effective for nonverbal skills, fi ndings\n" +
                                "from 1 study suggest that the use of gamma alternating with beta\n" +
                                "may enhance nonverbal performance skills.22 Findings regarding\n" +
                                "long-term stress/burnout were more mixed, but a benefi cial fi nding\n" +
                                "by Howard et al31 suggests that their protocol that began at 30\n" +
                                "Hz and was lowered until the subject was relaxed for 15 minutes\n" +
                                "and then used 8 to 14 Hz for 7 minutes might be worth further\n" +
                                "investigation. Specifi c protocols that used either delta and theta,23\n" +
                                "theta alone,20 mostly delta,30 or beta23 were ineffective in elevating\n" +
                                "mood in healthy adults, but there may be other protocols that help\n" +
                                "relieve depression in subjects diagnosed with depression.\n" +
                                "The immediate psychological effects of memory, attention,\n" +
                                "stress, pain, and headaches/migraines were shown to benefi t from\n" +
                                "even a single session of BWE. Many practitioners and developers\n" +
                                "of BWE tools believe that repeated exposure to BWE will allow the\n" +
                                "user to enter the desired brain states unassisted. Indeed, the study\n" +
                                "by Patrick,21 which found improvements in overall intelligence and\n" +
                                "behavior, gradually withdrew the stimulus until users could produce\n" +
                                "the targeted brainwave frequencies on their own. Most studies\n" +
                                "that examined long-term effects did not withdraw stimulus over\n" +
                                "a specifi ed time period before testing, so the duration of the effects\n" +
                                "are unclear. Nor are there studies that compare the effects of duration\n" +
                                "or frequency of stimulation, so it is not known whether there\n" +
                                "is a minimal length or frequency of entrainment required to\n" +
                                "achieve each positive outcome or if there is a limit to the intensity\n" +
                                "of symptom relief from BWE");
                        break;
                    case 12:
                        mHeaderText.setText("Conclusions");
                        mContentText.setText(
                                "Seventeen of the studies were developed to confi rm or challenge\n" +
                                "a hypothesis that a specifi c frequency or protocol would have\n" +
                                "a benefi cial effect on a specifi c outcome. Two studies generally\n" +
                                "explored the response of subjects to stimulation at specifi c frequencies.\n" +
                                "Another study that compared the outcome of beta to ");
                        break;
                    case 13:
                        mHeaderText.setText("References");
                        mContentText.setText("REFERENCES\n" +
                                "1. Bloom B, Dey AN, Freeman G. Summary health statistics for U.S. children: National Health\n" +
                                "Interview Survey, 2005. Vital Health Stat 10. 2006 Dec;(231):1-84.\n" +
                                "2. Janet P. Psychological Healing:A Historical and Clinical Study. Paul E, Paul C, trans. London: Allen &\n" +
                                "Unwin; 1925.\n" +
                                "3. Berger, H. uber das Elektroenzephalogramm des Menschen. Arch Psychiatr Nervenk. 1929;\n" +
                                "87:527-570.\n" +
                                "4. Adrian E, Mathews B. The Berger Rhythm: potential changes from the occipital lobes in man.\n" +
                                "Brain. 1934;57(4):355-384.\n" +
                                "5. Dempsey E, Morison R. The interation of certain spontaneous and induced cortical potentials.\n" +
                                "Am J Physiol. 1941;135:301-308.\n" +
                                "6. Chatrian GE, Petersen MC, Lazarte JA. Responses to clicks from the human brain: some depth\n" +
                                "electrographic observations. Electroencephalogr Clin Neurophysiol. 1960 May;12:479-489.\n" +
                                "7. Walter WG, Dovey VJ, Shipton H. Analysis of the electrical response of the human cortex to photic\n" +
                                "stimulation. Nature. 1946;158(4016):540-541.\n" +
                                "8. Kroger WS, Schneider SA. An electronic aid for hypnotic induction: a preliminary report.\n" +
                                "International Journal of Clinical and Experimental Hypnosis 1959;7:93-98.\n" +
                                "9. Williams P, West M. EEG responses to photic stimulation in persons experienced at meditation.\n" +
                                "Electroencephalogr Clin Neurophysiol. 1975;39(5):519-522.\n" +
                                "10. Oster G. Auditory beats in the brain. Sci Am. 1973;229(4):94-102.\n" +
                                "11. Demos JN. Getting Started With Neurofeedback. New York, NY: W.W. Norton & Company, Inc.; 2005: 71.\n" +
                                "12. Toman J. Flicker potentials and the alpha rhythm in man. J Neurophysiol. 1941;4(1):51-61.\n" +
                                "13. Nystrom SH. Effects of photic stimulation on neuronal activity and subjective experience in man.\n" +
                                "Acta Neurol Scand. 1966;42(5):505-514.\n" +
                                "14. Moruzzi G, Magoun HW. Brain stem reticular formation and activation of the EEG. 1949. J\n" +
                                "Neuropsychiatry Clin Neurosci. 1995;7(2):251-267.\n" +
                                "15. Rogers LJ, Walter DO. Methods for finding single generators, with application to auditory driving\n" +
                                "of the human EEG by complex stimuli. J Neurosci Methods. 1981;4(3):257-265.\n" +
                                "16. Williams JH. Frequency-specific effects of flicker on recognition memory. Neuroscience.\n" +
                                "2001;104(2):283-286.\n" +
                                "17. Rosenfeld JP, Reinhart AM, Srivastava S. The effects of alpha (10-Hz) and beta (22-Hz) entrainment\n" +
                                "stimulation on the alpha and beta EEG bands: individual differences are critical to prediction\n" +
                                "of effects. Appl Psychophysiol Biofeedback. 1997;22(1):3-20.\n" +
                                "18. Kumano H, Horie H, Kuboki T, et al. EEG-driven photic stimulation effect on plasma cortisol and\n" +
                                "beta-endorphin. Appl Psychophysiol Biofeedback. 1997;22(3):193-208.\n" +
                                "19. Joyce M, Siever D. Audio-Visual Entrainment (AVE) program as a treatment for behavior disorders\n" +
                                "in a school setting. J Neurother. 2000;4(2):9-25.\n" +
                                "20. Wahbeh H, Calabrese C, Zwickey H, Zajdel D. Binaural beat technology in humans: a pilot study\n" +
                                "to assess neuropsychologic, physiologic, and electroencephalographic effects. J Altern Complement\n" +
                                "Med. 2007;13(2):199-206.\n" +
                                "21. Patrick GJ. Improved neuronal regulation in ADHD: An application of 15 sessions of photic-drivPsychological\n" +
                                "Effects of Brainwave Entrainment\n" +
                                "TABLE 5 Mood*\n" +
                                "Study\n" +
                                "n & previous\n" +
                                "symptoms Study design\n" +
                                "Stimulation\n" +
                                "(Photic/\n" +
                                "Aud/AVE) Hz\n" +
                                "Duration\n" +
                                "of session\n" +
                                "Length of\n" +
                                "treatment Effect\n" +
                                "Proportion of\n" +
                                "signifi cant positive\n" +
                                "outcomes\n" +
                                "Lane,\n" +
                                "1998\n" +
                                "29 healthy\n" +
                                "adults\n" +
                                "Double-blind\n" +
                                "crossover;\n" +
                                "E1 vs E2\n" +
                                "Aud, BB Pink noise\n" +
                                "with BB or\n" +
                                "simple tones\n" +
                                "in beta (16-24\n" +
                                "Hz) or theta/\n" +
                                "delta (1.5-4\n" +
                                "Hz) through\n" +
                                "stereo headphones\n" +
                                "30 min Single\n" +
                                "session\n" +
                                "Comparing theta/\n" +
                                "delta to beta:\n" +
                                "Using POMS, confusion/\n" +
                                "bewilderment increased,\n" +
                                "fatigue/inertia increased,\n" +
                                "depression/rejection\n" +
                                "increased.\n" +
                                "0/6\n" +
                                "Wahbeh,\n" +
                                "2007a\n" +
                                "8 healthy\n" +
                                "adults\n" +
                                "Pre vs post Aud, BB\n" +
                                "with overlaying\n" +
                                "sounds\n" +
                                "of rain and\n" +
                                "bells\n" +
                                "Starts at\n" +
                                "10 Hz,\n" +
                                "decreases\n" +
                                "incrementally,\n" +
                                "staying at\n" +
                                "2.5 Hz for\n" +
                                "40 min\n" +
                                "60 min 60 sessions\n" +
                                "over 60\n" +
                                "days\n" +
                                "No effect with depression\n" +
                                "using BDI, total mood\n" +
                                "disturbance with POMS,\n" +
                                "or with depression using\n" +
                                "POMS\n" +
                                "0/8\n" +
                                "Wahbeh,\n" +
                                "2007b\n" +
                                "4 healthy\n" +
                                "adults\n" +
                                "Randomized\n" +
                                "double-blind\n" +
                                "crossover; E\n" +
                                "vs C\n" +
                                "BB E: 7 Hz with\n" +
                                "sound of rain\n" +
                                "C: sound of\n" +
                                "rain only\n" +
                                "30 min Single\n" +
                                "session\n" +
                                "No effect with total mood\n" +
                                "disturbance using POMS,\n" +
                                "Increase in depression in\n" +
                                "experimental condition\n" +
                                "using POMS\n" +
                                "0/7\n" +
                                "*POMS indicates Profi le of Mood States; BDI, Beck Depression Inventory.\n" +
                                "TK ALTERNATIVE THERAPIES, sep/oct 2008, VOL. 14, NO. 5 49\n" +
                                "en EEG neurotherapy. J Neurother. 1996;1(4):27-36.\n" +
                                "22. Olmstead R. Use of auditory and visual stimulation to improve cognitive abilities in learning-disabled\n" +
                                "children. J Neurother. 2005;9(2):49-61.\n" +
                                "23. Lane JD, Kasian SJ, Owens JE, Marsh GR. Binaural auditory beats affect vigilance performance\n" +
                                "and mood. Physiol Behav. 1998;63(2):249-252.\n" +
                                "24. Williams J, Ramaswamy D, Oulhaj A. 10 Hz flicker improves recognition memory in older people.\n" +
                                "BMC Neurosci. 2006 Mar 5;7:21.\n" +
                                "25. Budzynski T, Jordy J, Budzynski HK, Tang H, Claypoole K. Academic performance enhancement\n" +
                                "with photic stimulation and EDR feedback. J Neurother. 1999;3(3-4):11-21.\n" +
                                "26. Le Scouarnec RP, Poirier RM, Owens JE, Gauthier J, Taylor AG, Foresman PA. Use of binaural beat\n" +
                                "tapes for treatment of anxiety: a pilot study of tape preference and outcomes. Altern Ther Health\n" +
                                "Med. 2001;7(1):58-63.\n" +
                                "27. Padmanabhan R, Hildreth AJ, Laws D. A prospective, randomised, controlled study examining\n" +
                                "binaural beat audio and pre-operative anxiety in patients undergoing general anaesthesia for day\n" +
                                "case surgery. Anaesthesia. 2005;60(9):874-877.\n" +
                                "28. Ossebaard HC. Stress reduction by technology? An experimental study into the effects of brainmachines\n" +
                                "on burnout and state anxiety. Appl Psychophysiol Biofeedback. 2000;25(2):93-101.\n" +
                                "29. Morse DR, Chow E. The effect of the Relaxodont brain wave synchronizer on endodontic anxiety:\n" +
                                "evaluation by galvanic skin resistance, pulse rate, physical reactions, and questionnaire responses.\n" +
                                "Int J Psychosom. 1993;40(1-4):68-76.\n" +
                                "30. Wahbeh H, Calabrese C, Zwickey H. Binaural beat technology in humans: a pilot study to assess\n" +
                                "psychologic and physiologic effects. J Altern Complement Med. 2007;13(1):25-32.\n" +
                                "31. Howard CE, Graham LE, 2nd, Wycoff SJ. A comparison of methods for reducing stress among\n" +
                                "dental students. J Dent Educ. 1986;50(9):542-544.\n" +
                                "32. Nomura T, Higuchi K, Yu H, et al. Slow-wave photic stimulation relieves patient discomfort during\n" +
                                "esophagogastroduodenoscopy. J Gastroenterol Hepatol. 2006;21(1 Pt 1):54-58.\n" +
                                "33. Manns A, Miralles R, Adrian H. The application of audiostimulation and electromyographic biofeedback\n" +
                                "to bruxism and myofascial pain-dysfunction syndrome. Oral Surg Oral Med Oral Pathol.\n" +
                                "1981;52(3):247-252.\n" +
                                "34. Noton D. Migraine and photic stimulation: report on a survey of migraineurs using flickering\n" +
                                "light therapy. Complement Ther Nurs Midwifery. 2000;6(3):138-142.\n" +
                                "35. Solomon GD. Slow wave photic stimulation in the treatment of headachea preliminary report.\n" +
                                "Headache. 1985;25(8):444-446.\n" +
                                "36. Anderson DJ. The treatment of migraine with variable frequency photo-stimulation. Headache.\n" +
                                "1989;29(3):154-155.\n" +
                                "37. Anderson DJ, Legg NJ, Ridout DA. Preliminary trial of photic stimulation for premenstrual syndrome.\n" +
                                "J Obstet Gynaecol. 1997;17(1):7");
                        break;
                }
            }
        });

        return  v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
