// ********************************
// ***** OBSERVED PREDICATES  *****
// ********************************

// ALL TYPES USED 3 x 6 = 18 types
// ENTITIES: Concept1, DProp1, OProp1, Concept2, DProp2, OProp2
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


// **************************************
// ********* HIDDEN PREDICATES  *********
// **************************************

//  straight forward mapping between logical entities
conceptEQ(Concept1, Concept2)
dpropEQ(DProp1, DProp2)
opropEQ(OProp1, OProp2)

// mappings between labels
conceptLabelEQ(ConceptLabel1, ConceptLabel2)
dpropLabelEQ(DPropLabel1, DPropLabel2)
dpropLabelEQ(OPropLabel1, OPropLabel2)

// simple words that have the same meaning
conceptWordEQ(ConceptWord1, ConceptWord2)
dpropWordEQ(DPropWord1, DPropWord2)
opropWordEQ(OPropWord1, OPropWord2)

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
-0.001 conceptEQ(e_o1, e_o2)
-0.001 dpropEQ(e_o1, e_o2)
-0.001 opropEQ(e_o1, e_o2)


// ************************************************************
// ********* relation between words and labels ****************
// ************************************************************

// *** a matched label results in a matched entity ***

!conceptLabelEQ(l_o1, l_o2) v !conceptLabel_o1(e_o1, l_o1) v !conceptLabel_o2(e_o2, l_o2) v conceptEQ(e_o2, e_o1).
!dpropLabelEQ(l_o1, l_o2) v !dpropLabel_o1(e_o1, l_o1) v !dpropLabel_o2(e_o2, l_o2) v dpropEQ(e_o2, e_o1).
!opropLabelEQ(l_o1, l_o2) v !opropLabel_o1(e_o1, l_o1) v !opropLabel_o2(e_o2, l_o2) v opropEQ(e_o2, e_o1).

// *** the rules for matching 1-word labels on 1-word labels ***

!conceptL1Word_o1(e_o1, w_o1) v !conceptL1Word_o2(e_o2, w_o2) v !conceptWordEQ(w_o1, w_o2) v conceptLabelEQ(e_o1, e_o2). 
!dpropL1Word_o1(e_o1, w_o1) v !dpropL1Word_o2(e_o2, w_o2) v !dpropWordEQ(w_o1, w_o2) v propLabelEQ(e_o1, e_o2).
!opropL1Word_o1(e_o1, w_o1) v !opropL1Word_o2(e_o2, w_o2) v !opropWordEQ(w_o1, w_o2) v opropLabelEQ(e_o1, e_o2).

// *** the rules for matching 2-word labels on 2-word labels ***

!conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w2_o1, w2_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptLabelEQ(e_o1, e_o2).
!conceptWordEQ(w1_o1, w2_o2) v !conceptWordEQ(w2_o1, w1_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptLabelEQ(e_o1, e_o2).

!dpropWordEQ(w1_o1, w1_o2) v !dpropWordEQ(w2_o1, w2_o2) v !dpropL2Word_o1(e_o1, w1_o1, w2_o1) v !dpropL2Word_o2(e_o2, w1_o2, w2_o2) v dpropLabelEQ(e_o1, e_o2).
!dpropWordEQ(w1_o1, w2_o2) v !dpropWordEQ(w2_o1, w1_o2) v !dpropL2Word_o1(e_o1, w1_o1, w2_o1) v !dpropL2Word_o2(e_o2, w1_o2, w2_o2) v dpropLabelEQ(e_o1, e_o2).

!opropWordEQ(w1_o1, w1_o2) v !opropWordEQ(w2_o1, w2_o2) v !opropL2Word_o1(e_o1, w1_o1, w2_o1) v !opropL2Word_o2(e_o2, w1_o2, w2_o2) v opropLabelEQ(e_o1, e_o2).
!opropWordEQ(w1_o1, w2_o2) v !opropWordEQ(w2_o1, w1_o2) v !opropL2Word_o1(e_o1, w1_o1, w2_o1) v !opropL2Word_o2(e_o2, w1_o2, w2_o2) v opropLabelEQ(e_o1, e_o2).

// ** the rules for matching (and not matching) 2-word labels on 1-word labels 
!conceptWordEQ(w2_o1, w1_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL1Word_o2(e_o2, w1_o2) v conceptLabelEQ(e_o1, e_o2).
!dpropL2Word_o1(e_o1, w1_o1, w2_o1) v !dpropL1Word_o2(e_o2, w1_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL2Word_o1(e_o1, w1_o1, w2_o1) v !opropL1Word_o2(e_o2, w1_o2) v !opropLabelEQ(e_o1, e_o2).

// ** the rules for matching (and not matching) 1-word labels on 2-word labels
!conceptWordEQ(w1_o1, w2_o2) v !conceptL1Word_o1(e_o1, w1_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptLabelEQ(e_o1, e_o2).
!dpropL1Word_o1(e_o1, w1_o1) v !dpropL2Word_o2(e_o2, w1_o2, w2_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL1Word_o1(e_o1, w1_o1) v !opropL2Word_o2(e_o2, w1_o2, w2_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for (not) matching 1-word labels on 3-word labels ****
!conceptL1Word_o1(e_o1, w1_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptLabelEQ(e_o1, e_o2).
!dpropL1Word_o1(e_o1, w1_o1) v !dpropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL1Word_o1(e_o1, w1_o1) v !opropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for (not) matching 2-word labels on 3-word labels ****

!conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w2_o1, w3_o2) v !conceptL2Word_o1(e_o1, w1_o1, w2_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v conceptLabelEQ(e_o1, e_o2).
!dpropL2Word_o1(e_o1, w1_o1, w2_o1) v !dpropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL2Word_o1(e_o1, w1_o1, w2_o1) v !opropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for (not) matching 3-word labels on 1-word labels ****
!conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL1Word_o2(e_o2, w1_o2) v !conceptLabelEQ(e_o1, e_o2).
!dpropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dpropL1Word_o2(e_o2, w1_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !opropL1Word_o2(e_o2, w1_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for (not) matching 3-word labels on 2-word labels ****

!conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w3_o1, w2_o2) v !conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL2Word_o2(e_o2, w1_o2, w2_o2) v conceptLabelEQ(e_o1, e_o2).
!dpropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dpropL2Word_o2(e_o2, w1_o2, w2_o2) v !dpropLabelEQ(e_o1, e_o2).
!opropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !opropL2Word_o2(e_o2, w1_o2, w2_o2) v !opropLabelEQ(e_o1, e_o2).

// *** the rules for matching 3-word labels on 3-word labels ***


!conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w2_o1, w2_o2) v !conceptWordEQ(w3_o1, w3_o2) v conceptLabelEQ(e_o1, e_o2).
!conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w1_o2) v !conceptWordEQ(w3_o1, w2_o2) v !conceptWordEQ(w2_o1, w3_o2) v conceptLabelEQ(e_o1, e_o2).
!conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w3_o1, w1_o2) v !conceptWordEQ(w2_o1, w2_o2) v !conceptWordEQ(w1_o1, w3_o2) v conceptLabelEQ(e_o1, e_o2).
!conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w2_o1, w1_o2) v !conceptWordEQ(w1_o1, w2_o2) v !conceptWordEQ(w3_o1, w3_o2) v conceptLabelEQ(e_o1, e_o2).
!conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w3_o2) v !conceptWordEQ(w2_o1, w1_o2) v !conceptWordEQ(w3_o1, w2_o2) v conceptLabelEQ(e_o1, e_o2).
!conceptL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !conceptL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptWordEQ(w1_o1, w2_o2) v !conceptWordEQ(w2_o1, w3_o2) v !conceptWordEQ(w3_o1, w1_o2) v conceptLabelEQ(e_o1, e_o2).


!dpropWordEQ(w1_o1, w1_o2) v !dpropWordEQ(w2_o1, w2_o2) v !dpropWordEQ(w3_o1, w3_o2) v !dpropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dpropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v dpropLabelEQ(e_o1, e_o2).

!opropWordEQ(w1_o1, w1_o2) v !opropWordEQ(w2_o1, w2_o2) v !opropWordEQ(w3_o1, w3_o2) v !opropL3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !opropL3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v opropLabelEQ(e_o1, e_o2).

