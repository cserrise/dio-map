// ********************************
// ***** OBSERVED PREDICATES  *****
// ********************************

// ALL TYPES USED 3 x 6 = 18 types
/// ENTITIES: Concept1, DProp1, OProp1, Concept2, DProp2, OProp2
// LABELS: ConceptLabel1, ConceptLabel2, DPropLabel1, OPropLabel1, DPropLabel2, OPropLabel2
// WORDS: ConceptWord1, ConceptWord2, DPropWord1, OPropWord1, DPropWord2, OPropWord2

// *** ontology 1 ****

*conceptLabel_o1(Concept1, ConceptLabel1)
*dpropLabel_o1(DProp1, DPropLabel1)
*opropLabel_o1(OProp1, OPropLabel1)

*conceptL0Word_o1(ConceptLabel1)
*conceptL1Word_o1(ConceptLabel1, ConceptWord1)
*conceptL2Word_o1(ConceptLabel1, ConceptWord1, ConceptWord1)
*conceptL3Word_o1(ConceptLabel1, ConceptWord1, ConceptWord1, ConceptWord1)

*dpropL0Word_o1(DPropLabel1)
*dpropL1Word_o1(DPropLabel1, DPropWord1)
*dpropL2Word_o1(DPropLabel1, DPropWord1, DPropWord1)
*dpropL3Word_o1(DPropLabel1, DPropWord1, DPropWord1, DPropWord1)

*opropL0Word_o1(OPropLabel1)
*opropL1Word_o1(OPropLabel1, OPropWord1)
*opropL2Word_o1(OPropLabel1, OPropWord1, OPropWord1)
*opropL3Word_o1(OPropLabel1, OPropWord1, OPropWord1, OPropWord1)

// *** ontology 2 ****

*conceptLabel_o2(Concept2, ConceptLabel2)
*dpropLabel_o2(DProp2, DPropLabel2)
*opropLabel_o2(OProp2, OPropLabel2)

*conceptL0Word_o2(ConceptLabel2)
*conceptL1Word_o2(ConceptLabel2, ConceptWord2)
*conceptL2Word_o2(ConceptLabel2, ConceptWord2, ConceptWord2)
*conceptL3Word_o2(ConceptLabel2, ConceptWord2, ConceptWord2, ConceptWord2)

*dpropL0Word_o2(DPropLabel2)
*dpropL1Word_o2(DPropLabel2, DPropWord2)
*dpropL2Word_o2(DPropLabel2, DPropWord2, DPropWord2)
*dpropL3Word_o2(DPropLabel2, DPropWord2, DPropWord2, DPropWord2)

*opropL0Word_o2(OPropLabel2)
*opropL1Word_o2(OPropLabel2, OPropWord2)
*opropL2Word_o2(OPropLabel2, OPropWord2, OPropWord2)
*opropL3Word_o2(OPropLabel2, OPropWord2, OPropWord2, OPropWord2)

// *** similarity between words (typed by the) ****

*conceptWordSim(ConceptWord1, ConceptWord2, float_)
*dpropWordSim(DPropWord1, DPropWord2, float_)
*opropWordSim(OPropWord1, OPropWord2, float_)

// *** semantics ***

// sub params: more general, more specific, e.g. sub_o1(Person,Author)
*sub_o1(Concept1, Concept1)
*dsub_o1(Concept1, Concept1)
*dis_o1(Concept1, Concept1)
*sub_o2(Concept2, Concept2)
*dsub_o2(Concept2, Concept2)
*dis_o2(Concept2, Concept2)

*leafnode_o1(Concept1)
*leafnode_o2(Concept2)
*rootnode_o1(Concept1)
*rootnode_o2(Concept2)

*dpropDom_o1(DProp1, Concept1)
*opropDom_o1(OProp1, Concept1)
*opropRan_o1(OProp1, Concept1)

*dpropDom_o2(DProp2, Concept2)
*opropDom_o2(OProp2, Concept2)
*opropRan_o2(OProp2, Concept2)

*opropSub_o1(OProp1, OProp1)
*opropSub_o2(OProp2, OProp2)

*dpropSub_o1(DProp1, DProp1)
*dpropSub_o2(DProp2, DProp2)

*opropInv_o1(OProp1, OProp1)
*opropInv_o2(OProp2, OProp2)

// **************************************
// ********* HIDDEN PREDICATES  *********
// **************************************

//  straight forward mapping between logical entities
conceptEQ(Concept1, Concept2)
dpropEQ(DProp1, DProp2)
opropEQ(OProp1, OProp2)

// special helper construct
opropInvEQ(OProp1)

// true if the direct suoerclass is mapped by equivalence
conceptSUP(Concept1, Concept2)
// true if the direct subclass is mapped by equivalence
conceptSUB(Concept1, Concept2)
// true if on both sides sub and superclass are mapped (so its even more far away)
conceptXEQ(Concept1, Concept2)

// another helper construct, used to ignore the score of mapping inverse properties
conceptEQIgnored(Concept1, Concept2)

// mappings between labels
conceptLabelEQ(ConceptLabel1, ConceptLabel2)
dpropLabelEQ(DPropLabel1, DPropLabel2)
dpropLabelEQ(OPropLabel1, OPropLabel2)

// ignoring single words
conceptWordIgnore_o1(ConceptWord1)
conceptWordIgnore_o2(ConceptWord2)
dpropWordIgnore_o1(DPropWord1)
dpropWordIgnore_o2(DPropWord2)
opropWordIgnore_o1(OPropWord1)
opropWordIgnore_o2(OPropWord2)

// simple words that have the same meaning
conceptWordEQ(ConceptWord1, ConceptWord2)
dpropWordEQ(DPropWord1, DPropWord2)
opropWordEQ(OPropWord1, OPropWord2)

// two words (form a label) have the same meaning as two words (from another label)
conceptWordEQ22aAbB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ22aBbA(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)

// two words (form a label) have the same meaning as three words (from another label)
conceptWordEQ23aAbCxB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord2)
conceptWordEQ32aAbXcB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1)

// three words (form a label) have the same meaning as three words (from another label)
conceptWordEQ33aAbBcC(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aCbAcB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aBbCcA(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aAbCcB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aCbBcA(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aBbAcC(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)

// *******************************
// ********* programm ************
// *******************************

// **********************************
// ***** WEIGHTS ARE MY WORLD *******
// **********************************

// add the similarity of two words to the objective, if they are set equiv in the solution
sim: !conceptWordSim(w1, w2, sim) v conceptWordEQ(w1,w2)
sim: !dpropWordSim(w1, w2, sim) v dpropWordEQ(w1,w2)
sim: !opropWordSim(w1, w2, sim) v opropWordEQ(w1,w2)

// add this score for every concept equiv (= correspondence) that was generated 
0.5 conceptEQ(e_o1, e_o2)
0.499 dpropEQ(e_o1, e_o2)
0.499 opropEQ(e_o1, e_o2)

// give a minus to subsumption mappings whenerv they ar required to generate property mappings
// this means that mapping properties that have only idrect relations between their domain and range
// are slightly punished
-0.1 conceptSUB(e_o1, e_o2)
-0.1 conceptXEQ(e_o1, e_o2)

// blind words should not occur, setting a word blind has to be punished
-0.75 conceptWordIgnore_o1(w)
-0.75 conceptWordIgnore_o2(w)
-0.75 dpropWordIgnore_o1(w)
-0.75 dpropWordIgnore_o2(w)
-0.75 opropWordIgnore_o1(w)
-0.75 opropWordIgnore_o2(w)

// give a minus to mappings of inverse properties, this takes into account that factual only one
// of the properties is mapped, while the other mapping is derived (kind of derived)
!opropInv_o1(e1_o1, e2_o1) v !opropInv_o2(e1_o2, e2_o2) v !opropEQ(e1_o1, e1_o2) v !opropEQ(e2_o1, e2_o2) v opropInvEQ(e1_o1).
-0.24 opropInvEQ(e1_o1)

// give some minus for 2 on 3 and 3 on 2
-0.1 conceptWordEQ23aAbCxB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o2)
-0.1 conceptWordEQ32aAbXcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1)
// give some minus for reversing words in 2on 2
-0.1 conceptWordEQ22aBbA(w1_o1, w1_o2, w2_o1, w2_o2)
// give some minus for flips in 3 on 3 
-0.05 conceptWordEQ33aCbAcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2)
-0.05 conceptWordEQ33aBbCcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2)
-0.05 conceptWordEQ33aAbCcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2)
-0.05 conceptWordEQ33aCbBcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2)
-0.05 conceptWordEQ33aBbAcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2)

// give a minimal minus to alwas prefer that the more direct match (Paper = Paper) is preferred to
// the more complex one (ConferencePaper = Paper), even though the modifier might be already "ignored"
-0.01 conceptEQIgnored(e_o1, e_o2)

// ************************************************************
// ********* relation between entities and labels *************
// ************************************************************

// HERE IS WHERE THE RUBBER HITS THE ROAD

// --- do not remove the following three lines ---
// this line is converted to a set of fomulae that corresponds to an existential quantified formula

// ? !conceptEQ(e_o1, e_o2) v conceptLabelEQ([conceptLabel_o1(e_o1,?)], [conceptLabel_o2(e_o2,?)])
// ? !dpropEQ(e_o1, e_o2) v dpropLabelEQ([dpropLabel_o1(e_o1,?)], [dpropLabel_o2(e_o2,?)])
// ? !opropEQ(e_o1, e_o2) v opropLabelEQ([opropLabel_o1(e_o1,?)], [opropLabel_o2(e_o2,?)])

// ************************************************************
// ********* relation between words and labels ****************
// ************************************************************

// *** the rule for matching 0-word entities on 0/1/2/3-word entities (never match this) ***
!conceptL0Word_o1(e_o1) v !conceptL0Word_o2(e_o2) v !conceptLabelEQ(e_o1, e_o2).
!dpropL0Word_o1(e_o1) v !dpropL0Word_o2(e_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL0Word_o1(e_o1) v !opropL0Word_o2(e_o2) v !opropLabelEQ(e_o1, e_o2).

!conceptL0Word_o1(e_o1) v !conceptL1Word_o2(e_o2, x) v !conceptLabelEQ(e_o1, e_o2).
!dpropL0Word_o1(e_o1) v !dpropL1Word_o2(e_o2, x) v !dpropLabelEQ(e_o1, e_o2).
!opropL0Word_o1(e_o1) v !opropL1Word_o2(e_o2, x) v !opropLabelEQ(e_o1, e_o2).

!conceptL1Word_o1(e_o1, x) v !conceptL0Word_o2(e_o2) v !conceptLabelEQ(e_o1, e_o2).
!dpropL1Word_o1(e_o1, x) v !dpropL0Word_o2(e_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL1Word_o1(e_o1, x) v !opropL0Word_o2(e_o2) v !opropLabelEQ(e_o1, e_o2).

!conceptL0Word_o1(e_o1) v !conceptL2Word_o2(e_o2, x,y) v !conceptEQ(e_o1, e_o2).
!dpropL0Word_o1(e_o1) v !dpropL2Word_o2(e_o2, x,y) v !dpropLabelEQ(e_o1, e_o2).
!opropL0Word_o1(e_o1) v !opropL2Word_o2(e_o2, x,y) v !opropLabelEQ(e_o1, e_o2).

!conceptL2Word_o1(e_o1, x,y) v !conceptL0Word_o2(e_o2) v !conceptLabelEQ(e_o1, e_o2).
!dpropL2Word_o1(e_o1, x,y) v !dpropL0Word_o2(e_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL2Word_o1(e_o1, x,y) v !opropL0Word_o2(e_o2) v !opropLabelEQ(e_o1, e_o2).

!conceptL0Word_o1(e_o1) v !conceptL3Word_o2(e_o2, x, y, z) v !conceptLabelEQ(e_o1, e_o2).
!dpropL0Word_o1(e_o1) v !dpropL3Word_o2(e_o2, x, y, z) v !dpropLabelEQ(e_o1, e_o2).
!opropL0Word_o1(e_o1) v !opropL3Word_o2(e_o2, x, y, z) v !opropLabelEQ(e_o1, e_o2).

!conceptL3Word_o1(e_o1, x, y, z) v !conceptL0Word_o2(e_o2) v !conceptLabelEQ(e_o1, e_o2).
!dpropL3Word_o1(e_o1, x, y, z) v !dpropL0Word_o2(e_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL3Word_o1(e_o1, x, y, z) v !opropL0Word_o2(e_o2) v !opropLabelEQ(e_o1, e_o2).


// *** the rules for matching 1-word entities on 1-word entities ***
!conceptLabelEQ(e_o1, e_o2) v !conceptL1Word_o1(e_o1, w_o1) v !conceptL1Word_o2(e_o2, w_o2) v conceptWordEQ(w_o1, w_o2).
!dpropLabelEQ(e_o1, e_o2) v !dpropL1Word_o1(e_o1, w_o1) v !dpropL1Word_o2(e_o2, w_o2) v dpropWordEQ(w_o1, w_o2).
!opropLabelEQ(e_o1, e_o2) v !opropL1Word_o1(e_o1, w_o1) v !opropL1Word_o2(e_o2, w_o2) v opropWordEQ(w_o1, w_o2).

// *** the rules for matching 2-word entities on 2-word entities ***

!conceptLabelEQ(e_o1, e_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptWordEQ22aAbB(w1_o1, w1_o2, w2_o1, w2_o2) v conceptWordEQ22aBbA(w1_o1, w1_o2, w2_o1, w2_o2).

!dpropLabelEQ(e_o1, e_o2) v !dpropL2Word_o1(e_o1, w1_o1, w2_o1) v !dpropL2Word_o2(e_o2, w1_o2, w2_o2) v dpropWordEQ(w1_o1, w1_o2).
!dpropLabelEQ(e_o1, e_o2) v !dpropL2Word_o1(e_o1, w1_o1, w2_o1) v !dpropL2Word_o2(e_o2, w1_o2, w2_o2) v dpropWordEQ(w2_o1, w2_o2).

!opropLabelEQ(e_o1, e_o2) v !opropL2Word_o1(e_o1, w1_o1, w2_o1) v !opropL2Word_o2(e_o2, w1_o2, w2_o2) v opropWordEQ(w1_o1, w1_o2).
!opropLabelEQ(e_o1, e_o2) v !opropL2Word_o1(e_o1, w1_o1, w2_o1) v !opropL2Word_o2(e_o2, w1_o2, w2_o2) v opropWordEQ(w2_o1, w2_o2).

// ** the rules for matching (and not matching) 2-word entities on 1-word entities 
!conceptLabelEQ(e_o1, e_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL1Word_o2(e_o2, w1_o2) v conceptWordEQ(w2_o1, w1_o2).
!conceptLabelEQ(e_o1, e_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL1Word_o2(e_o2, w1_o2) v conceptWordIgnore_o1(w1_o1).
!conceptLabelEQ(e_o1, e_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL1Word_o2(e_o2, w1_o2) v conceptEQIgnored(e_o1, e_o2).

!dpropL2Word_o1(e_o1, w1_o1, w2_o1) v !dpropL1Word_o2(e_o2, w1_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL2Word_o1(e_o1, w1_o1, w2_o1) v !opropL1Word_o2(e_o2, w1_o2) v !opropLabelEQ(e_o1, e_o2).

// ** the rules for matching (and not matching) 1-word entities on 2-word entities
!conceptLabelEQ(e_o1, e_o2) v !conceptL1Word_o1(e_o1, w1_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptWordEQ(w1_o1, w2_o2).
!conceptLabelEQ(e_o1, e_o2) v !conceptL1Word_o1(e_o1, w1_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptWordIgnore_o2(w1_o2).
!conceptLabelEQ(e_o1, e_o2) v !conceptL1Word_o1(e_o1, w1_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptEQIgnored(e_o1, e_o2).

!dpropL1Word_o1(e_o1, w1_o1) v !dpropL2Word_o2(e_o2, w1_o2, w2_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL1Word_o1(e_o1, w1_o1) v !opropL2Word_o2(e_o2, w1_o2, w2_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for (not) matching 1-word entities on 3-word entities ****
!conceptL1Word_o1(e_o1, w1_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptLabelEQ(e_o1, e_o2).
!dpropL1Word_o1(e_o1, w1_o1) v !dpropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL1Word_o1(e_o1, w1_o1) v !opropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for (not) matching 2-word entities on 3-word entities ****


!conceptLabelEQ(e_o1, e_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v conceptWordEQ23aAbCxB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o2).

!dpropL2Word_o1(e_o1, w1_o1, w2_o1) v !dpropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL2Word_o1(e_o1, w1_o1, w2_o1) v !opropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for (not) matching 3-word labels on 1-word labels ****
!conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL1Word_o2(e_o2, w1_o2) v !conceptLabelEQ(e_o1, e_o2).
!dpropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dpropL1Word_o2(e_o2, w1_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !opropL1Word_o2(e_o2, w1_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for (not) matching 3-word labels on 2-word labels ****

!conceptLabelEQ(e_o1, e_o2) v !conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptWordEQ32aAbXcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1).

!dpropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dpropL2Word_o2(e_o2, w1_o2, w2_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !opropL2Word_o2(e_o2, w1_o2, w2_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for matching 3-word labels on 3-word labels ***

!conceptLabelEQ(e_o1, e_o2) v !conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v conceptWordEQ33aAbBcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aCbAcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aBbCcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aAbCcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aCbBcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aBbAcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2).

!dpropLabelEQ(e_o1, e_o2) v !dpropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dpropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v dpropWordEQ(w1_o1, w1_o2).
!dpropLabelEQ(e_o1, e_o2) v !dpropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dpropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v dpropWordEQ(w2_o1, w2_o2).
!dpropLabelEQ(e_o1, e_o2) v !dpropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dpropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v dpropWordEQ(w3_o1, w3_o2).

!opropLabelEQ(e_o1, e_o2) v !opropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !opropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v opropWordEQ(w1_o1, w1_o2).
!opropLabelEQ(e_o1, e_o2) v !opropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !opropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v opropWordEQ(w2_o1, w2_o2).
!opropLabelEQ(e_o1, e_o2) v !opropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !opropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v opropWordEQ(w3_o1, w3_o2).

// *** helper constructs and rules that establish the linking ***

!conceptWordEQ22aAbB(w1_o1, w1_o2, w2_o1, w2_o2) v conceptWordEQ(w1_o1, w1_o2).
!conceptWordEQ22aAbB(w1_o1, w1_o2, w2_o1, w2_o2) v conceptWordEQ(w2_o1, w2_o2).

!conceptWordEQ22aBbA(w1_o1, w1_o2, w2_o1, w2_o2) v conceptWordEQ(w1_o1, w2_o2).
!conceptWordEQ22aBbA(w1_o1, w1_o2, w2_o1, w2_o2) v conceptWordEQ(w2_o1, w1_o2).

!conceptWordEQ33aAbBcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w1_o1, w1_o2).
!conceptWordEQ33aAbBcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w2_o1, w2_o2).
!conceptWordEQ33aAbBcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w3_o1, w3_o2).

!conceptWordEQ33aCbAcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w1_o1, w3_o2).
!conceptWordEQ33aCbAcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w2_o1, w1_o2).
!conceptWordEQ33aCbAcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w3_o1, w2_o2).

!conceptWordEQ33aBbCcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w1_o1, w2_o2).
!conceptWordEQ33aBbCcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w2_o1, w3_o2).
!conceptWordEQ33aBbCcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w3_o1, w1_o2).

!conceptWordEQ33aAbCcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w1_o1, w1_o2).
!conceptWordEQ33aAbCcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w2_o1, w3_o2).
!conceptWordEQ33aAbCcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w3_o1, w2_o2).

!conceptWordEQ33aCbBcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w1_o1, w3_o2).
!conceptWordEQ33aCbBcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w2_o1, w2_o2).
!conceptWordEQ33aCbBcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w3_o1, w1_o2).

!conceptWordEQ33aBbAcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w1_o1, w2_o2).
!conceptWordEQ33aBbAcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w2_o1, w1_o2).
!conceptWordEQ33aBbAcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ(w3_o1, w3_o2).

!conceptWordEQ23aAbCxB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o2) v conceptWordEQ(w1_o1, w1_o2).
!conceptWordEQ23aAbCxB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o2) v conceptWordEQ(w2_o1, w3_o2).

!conceptWordEQ32aAbXcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1) v conceptWordEQ(w1_o1, w1_o2).
!conceptWordEQ32aAbXcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1) v conceptWordEQ(w3_o1, w2_o2).



// *************************************************
// ** ALL KINDS OF CONSTRAINTS ON ENTITY LEVEL *****
// *************************************************

//  COHERENCY CONSTRAINTS FOR CONCEPT MAPPINGS
!conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !sub_o1(c1_o1, c2_o1) v !dis_o2(c1_o2, c2_o2).
!conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !dis_o1(c1_o1, c2_o1) v !sub_o2(c1_o2, c2_o2).

// STABILITY CONSTRAINTS FOR DATA PROPERTY MAPPINGS
// domain (there is no range, its a datatype)
!dpropEQ(p_o1, p_o2) v !dpropDom_o1(p_o1, c_o1) v !dpropDom_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2).

// WEAKEND STABILITY CONSTRAINTS FOR OBJECT PROPERTY MAPPINGS 
// domain
!opropEQ(p_o1, p_o2) v !opropDom_o1(p_o1, c_o1) v !opropDom_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2) v conceptSUB(c_o1, c_o2) v conceptSUP(c_o1, c_o2) v conceptXEQ(c_o1, c_o2).
// range
!opropEQ(p_o1, p_o2) v !opropRan_o1(p_o1, c_o1) v !opropRan_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2) v conceptSUB(c_o1, c_o2) v conceptSUP(c_o1, c_o2)  v conceptXEQ(c_o1, c_o2).

// forbids SUB od SUP mappings if there is not a subsumption
// relationship under the assumption that an EQ mapping is given
// are these formulae required ? at least they do no hurt
!conceptEQ(e1_o1, e_o2) v sub_o1(e1_o1, e2_o1) v !conceptSUP(e2_o1, e_o2).
!conceptEQ(e2_o1, e_o2) v sub_o1(e1_o1, e2_o1) v !conceptSUB(e1_o1, e_o2).
!conceptEQ(e_o1, e1_o2) v sub_o2(e1_o2, e2_o2) v !conceptSUB(e_o1, e2_o2).
!conceptEQ(e_o1, e2_o2) v sub_o2(e1_o2, e2_o2) v !conceptSUP(e_o1, e1_o2).

// enforces the existence of an EQ mapping in the "neighborhood" of a SUB/SUP mapping
!conceptSUP(e_o1, e_o2) v !dsub_o1(eg_o1, e_o1) v !dsub_o2(e_o2, es_o2) v conceptEQ(eg_o1, e_o2) v conceptEQ(e_o1, es_o2).
!conceptSUP(e_o1, e_o2) v !rootnode_o1(e_o1) v !dsub_o2(e_o2, es_o2) v conceptEQ(e_o1, es_o2).
!conceptSUP(e_o1, e_o2) v !dsub_o1(eg_o1, e_o1) v !leafnode_o2(e_o2) v conceptEQ(eg_o1, e_o2).
!rootnode_o1(e_o1) v !leafnode_o2(e_o2) v !conceptSUP(e_o1, e_o2).

!conceptSUB(e_o1, e_o2) v !dsub_o1(e_o1, es_o1) v !dsub_o2(eg_o2, e_o2) v conceptEQ(e_o1, eg_o2) v conceptEQ(es_o1, e_o2).
!conceptSUB(e_o1, e_o2) v !leafnode_o1(e_o1) v !dsub_o2(eg_o2, e_o2) v conceptEQ(e_o1, eg_o2).
!conceptSUB(e_o1, e_o2) v !dsub_o1(e_o1, es_o1) v !rootnode_o2(e_o2) v conceptEQ(es_o1, e_o2).
!leafnode_o1(e_o1) v !rootnode_o2(e_o2) v !conceptSUB(e_o1, e_o2).

!rootnode_o1(e_o1) v !conceptXEQ(e_o1, e_o2).
!rootnode_o2(e_o2) v !conceptXEQ(e_o1, e_o2).
!conceptXEQ(e_o1, e_o2) v !dsub_o1(eg_o1, e_o1) v !dsub_o2(eg_o2, e_o2) v conceptEQ(eg_o1, eg_o2).


// THE FAMOUS 1:1 RULE
|c_o1| conceptEQ(c_o1,c_o2) <= 1
|c_o2| conceptEQ(c_o1,c_o2) <= 1

|p_o1| dpropEQ(p_o1,p_o2) <= 1
|p_o2| dpropEQ(p_o1,p_o2) <= 1

|p_o1| opropEQ(p_o1,p_o2) <= 1
|p_o2| opropEQ(p_o1,p_o2) <= 1

