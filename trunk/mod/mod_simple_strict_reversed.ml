// ********************************
// ***** OBSERVED PREDICATES  *****
// ********************************

// *** Observed relations between logical entities and words ***

*concept0Word_o1(Concept1)
*concept1Word_o1(Concept1, ConceptWord1)
*concept2Word_o1(Concept1, ConceptWord1, ConceptWord1)
*concept3Word_o1(Concept1, ConceptWord1, ConceptWord1, ConceptWord1)

*dprop0Word_o1(DProp1)
*dprop1Word_o1(DProp1, DPropWord1)
*dprop2Word_o1(DProp1, DPropWord1, DPropWord1)
*dprop3Word_o1(DProp1, DPropWord1, DPropWord1, DPropWord1)

*oprop0Word_o1(OProp1)
*oprop1Word_o1(OProp1, OPropWord1)
*oprop2Word_o1(OProp1, OPropWord1, OPropWord1)
*oprop3Word_o1(OProp1, OPropWord1, OPropWord1, OPropWord1)

*concept0Word_o2(Concept2)
*concept1Word_o2(Concept2, ConceptWord2)
*concept2Word_o2(Concept2, ConceptWord2, ConceptWord2)
*concept3Word_o2(Concept2, ConceptWord2, ConceptWord2, ConceptWord2)

*dprop0Word_o2(DProp2)
*dprop1Word_o2(DProp2, DPropWord2)
*dprop2Word_o2(DProp2, DPropWord2, DPropWord2)
*dprop3Word_o2(DProp2, DPropWord2, DPropWord2, DPropWord2)

*oprop0Word_o2(OProp2)
*oprop1Word_o2(OProp2, OPropWord2)
*oprop2Word_o2(OProp2, OPropWord2, OPropWord2)
*oprop3Word_o2(OProp2, OPropWord2, OPropWord2, OPropWord2)

// *** similarity between words (typed by the) ****

*conceptWordSim(ConceptWord1, ConceptWord2, float_)
*dpropWordSim(DPropWord1, DPropWord2, float_)
*opropWordSim(OPropWord1, OPropWord2, float_)

// semantics

// sub params: more general, more specific, e.g. sub_o1(Person,Author)
*sub_o1(Concept1, Concept1)
*dis_o1(Concept1, Concept1)
*sub_o2(Concept2, Concept2)
*dis_o2(Concept2, Concept2)

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

// ********************************
// ***** HIDDEN PREDICATES  *******
// ********************************

conceptWordEQ(ConceptWord1, ConceptWord2)
dpropWordEQ(DPropWord1, DPropWord2)
opropWordEQ(OPropWord1, OPropWord2)

conceptEQ(Concept1, Concept2)
dpropEQ(DProp1, DProp2)
opropEQ(OProp1, OProp2)

conceptWordIgnore_o1(ConceptWord1)
conceptWordIgnore_o2(ConceptWord2)
dpropWordIgnore_o1(DPropWord1)
dpropWordIgnore_o2(DPropWord2)
opropWordIgnore_o1(OPropWord1)
opropWordIgnore_o2(OPropWord2)


// *******************************
// ********* programm ************
// *******************************


// add this score for every concept equiv (= correspondence) that was generated 
-0.001 conceptEQ(e_o1, e_o2)
-0.001 dpropEQ(e_o1, e_o2)
-0.001 opropEQ(e_o1, e_o2)

// blind words should not occur, setting a word blind has to be punished
conceptWordIgnore_o1(w).
conceptWordIgnore_o2(w).
dpropWordIgnore_o1(w).
dpropWordIgnore_o2(w).
opropWordIgnore_o1(w).
opropWordIgnore_o2(w).

// add the similarity of two words to the objective, if they are set equiv in the solution
sim: !conceptWordSim(w1, w2, sim) v conceptWordEQ(w1,w2)
sim: !dpropWordSim(w1, w2, sim) v dpropWordEQ(w1,w2)
sim: !opropWordSim(w1, w2, sim) v opropWordEQ(w1,w2)

// ********* relation between words and entities ****************


// *** the rule for matching 0-word entities on 0/1/2/3-word entities (never match this) ***


// *** the rules for matching 1-word entities on 1-word entities ***
!concept1Word_o1(e_o1, w_o1) v !concept1Word_o2(e_o2, w_o2) v !conceptWordEQ(w_o1, w_o2) v conceptEQ(e_o1, e_o2).

!dprop1Word_o1(e_o1, w_o1) v !dprop1Word_o2(e_o2, w_o2) v !dpropWordEQ(w_o1, w_o2) v !dpropDom_o1(e_o1, c_o1) v !dpropDom_o2(e_o2, c_o2) v !conceptEQ(c_o1, c_o2) v dpropEQ(e_o1, e_o2).

!oprop1Word_o1(e_o1, w_o1) v !oprop1Word_o2(e_o2, w_o2) v !opropWordEQ(w_o1, w_o2) v !opropDom_o1(e_o1, cd_o1) v !opropDom_o2(e_o2, cd_o2) v !conceptEQ(cd_o1, cd_o2) v !opropRan_o1(e_o1, cr_o1) v !opropRan_o2(e_o2, cr_o2) v !conceptEQ(cr_o1, cr_o2) v opropEQ(e_o1, e_o2).

// *** the rules for matching 2-word entities on 2-word entities ***
!concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v !conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w2_o1, w2_o2) v conceptEQ(e_o1, e_o2).
// flip words
!concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v !conceptWordEQ(w1_o1, w2_o2) v !conceptWordEQ(w2_o1, w1_o2) v conceptEQ(e_o1, e_o2).

!dprop2Word_o1(e_o1, w1_o1, w2_o1) v !dprop2Word_o2(e_o2, w1_o2, w2_o2) v !dpropWordEQ(w1_o1, w1_o2) v !dpropWordEQ(w2_o1, w2_o2) v !dpropDom_o1(e_o1, c_o1) v !dpropDom_o2(e_o2, c_o2) v !conceptEQ(c_o1, c_o2) v dpropEQ(e_o1, e_o2).

!oprop2Word_o1(e_o1, w1_o1, w2_o1) v !oprop2Word_o2(e_o2, w1_o2, w2_o2) v !opropWordEQ(w1_o1, w1_o2) v !opropWordEQ(w2_o1, w2_o2) v !opropDom_o1(e_o1, cd_o1) v !opropDom_o2(e_o2, cd_o2) v !conceptEQ(cd_o1, cd_o2) v !opropRan_o1(e_o1, cr_o1) v !opropRan_o2(e_o2, cr_o2) v !conceptEQ(cr_o1, cr_o2)v opropEQ(e_o1, e_o2).


// ** the rules for matching (and not matching) 2-word entities on 1-word entities 
!concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept1Word_o2(e_o2, w1_o2) v !conceptWordEQ(w2_o1, w1_o2) v !conceptWordIgnore_o1(w1_o1) v conceptEQ(e_o1, e_o2).

!dprop2Word_o1(e_o1, w1_o1, w2_o1) v !dprop1Word_o2(e_o2, w1_o2) v !dpropEQ(e_o1, e_o2).
!oprop2Word_o1(e_o1, w1_o1, w2_o1) v !oprop1Word_o2(e_o2, w1_o2) v !opropEQ(e_o1, e_o2).

// ** the rules for matching (and not matching) 1-word entities on 2-word entities
!concept1Word_o1(e_o1, w1_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v !conceptWordEQ(w1_o1, w2_o2) v !conceptWordIgnore_o2(w1_o2) v conceptEQ(e_o1, e_o2).

!dprop1Word_o1(e_o1, w1_o1) v !dprop2Word_o2(e_o2, w1_o2, w2_o2) v !dpropEQ(e_o1, e_o2).
!oprop1Word_o1(e_o1, w1_o1) v !oprop2Word_o2(e_o2, w1_o2, w2_o2) v !opropEQ(e_o1, e_o2).

// *** the rules for (not) matching 1-word entities on 3-word entities ****
!concept1Word_o1(e_o1, w1_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptEQ(e_o1, e_o2).
!dprop1Word_o1(e_o1, w1_o1) v !dprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !dpropEQ(e_o1, e_o2).
!oprop1Word_o1(e_o1, w1_o1) v !oprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !opropEQ(e_o1, e_o2).

// *** the rules for (not) matching 2-word entities on 3-word entities ****

!concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w2_o1, w3_o2) v conceptEQ(e_o1, e_o2).

// *** the rules for (not) matching 3-word labels on 1-word labels ****


// *** the rules for (not) matching 3-word labels on 2-word labels ****

!concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v !conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w3_o1, w2_o2) v conceptEQ(e_o1, e_o2).


// *** the rules for matching 3-word labels on 3-word labels ***
!concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w2_o1, w2_o2) v !conceptWordEQ(w3_o1, w3_o2) v conceptEQ(e_o1, e_o2).

!concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w2_o2) v !conceptWordEQ(w2_o1, w1_o2) v !conceptWordEQ(w3_o1, w3_o2) v conceptEQ(e_o1, e_o2).

!concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w2_o1, w3_o2) v !conceptWordEQ(w3_o1, w2_o2) v conceptEQ(e_o1, e_o2).

!concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w2_o2) v !conceptWordEQ(w2_o1, w3_o2) v !conceptWordEQ(w3_o1, w1_o2) v conceptEQ(e_o1, e_o2).

!concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w3_o2) v !conceptWordEQ(w2_o1, w1_o2) v !conceptWordEQ(w3_o1, w2_o2) v conceptEQ(e_o1, e_o2).

!concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w3_o2) v !conceptWordEQ(w2_o1, w2_o2) v !conceptWordEQ(w3_o1, w1_o2) v conceptEQ(e_o1, e_o2).


// --------------

!dprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !dpropWordEQ(w1_o1, w1_o2) v !dpropWordEQ(w2_o1, w2_o2) v !dpropWordEQ(w3_o1, w3_o2) v !dpropDom_o1(e_o1, c_o1) v !dpropDom_o2(e_o2, c_o2) v !conceptEQ(c_o1, c_o2) v dpropEQ(e_o1, e_o2).

!oprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !oprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !opropWordEQ(w1_o1, w1_o2) v !opropWordEQ(w2_o1, w2_o2) v !opropWordEQ(w3_o1, w3_o2) v !opropDom_o1(e_o1, cd_o1) v !opropDom_o2(e_o2, cd_o2) v !conceptEQ(cd_o1, cd_o2) v !opropRan_o1(e_o1, cr_o1) v !opropRan_o2(e_o2, cr_o2) v !conceptEQ(cr_o1, cr_o2) v opropEQ(e_o1, e_o2).


// *******************************
// ** coherency constraints *****
// *******************************
// assumption: the logic dependencies are already materialized in advance for all relevant predicates

// CONCEPT PATTERN COHERENCY
!conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !sub_o1(c1_o1, c2_o1) v !dis_o2(c1_o2, c2_o2).
!conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !dis_o1(c1_o1, c2_o1) v !sub_o2(c1_o2, c2_o2).
 
// CONCEPT PATTERN COHERENCY
// !conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !sub_o1(c1_o1, c2_o1) v !dis_o2(c1_o2, c2_o2).
// !conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !dis_o1(c1_o1, c2_o1) v !sub_o2(c1_o2, c2_o2).

// STABILITY CONSTRAINTS FOR PROPERTY MAPPINGS
!opropEQ(p_o1, p_o2) v !opropDom_o1(p_o1, c_o1) v !opropDom_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2).



!opropEQ(p_o1, p_o2) v !opropRan_o1(p_o1, c_o1) v !opropRan_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2).




!dpropEQ(p_o1, p_o2) v !dpropDom_o1(p_o1, c_o1) v !dpropDom_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2).


