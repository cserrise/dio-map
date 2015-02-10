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

// ********************************
// ***** HIDDEN PREDICATES  *******
// ********************************

conceptWordEQ(ConceptWord1, ConceptWord2)
dpropWordEQ(DPropWord1, DPropWord2)
opropWordEQ(OPropWord1, OPropWord2)

conceptWordEQ22aAbB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ22aBbA(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)


conceptWordEQ23aAbCxB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord2)
conceptWordEQ32aAbXcB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1)



conceptWordEQ33aAbBcC(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aCbAcB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aBbCcA(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aAbCcB(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aCbBcA(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)
conceptWordEQ33aBbAcC(ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2, ConceptWord1, ConceptWord2)

conceptEQ(Concept1, Concept2)
dpropEQ(DProp1, DProp2)
opropEQ(OProp1, OProp2)
opropInvEQ(OProp1)

conceptXEQ(Concept1, Concept2)
conceptSUP(Concept1, Concept2)
conceptSUB(Concept1, Concept2)

conceptEQIgnored(Concept1, Concept2)

conceptWordIgnore_o1(ConceptWord1)
conceptWordIgnore_o2(ConceptWord2)
dpropWordIgnore_o1(DPropWord1)
dpropWordIgnore_o2(DPropWord2)
opropWordIgnore_o1(OPropWord1)
opropWordIgnore_o2(OPropWord2)


// *******************************
// ********* programm ************
// *******************************

// create a 1:1 alignment 
|c_o1| conceptEQ(c_o1,c_o2) <= 1
|c_o2| conceptEQ(c_o1,c_o2) <= 1

|p_o1| dpropEQ(p_o1,p_o2) <= 1
|p_o2| dpropEQ(p_o1,p_o2) <= 1

|p_o1| opropEQ(p_o1,p_o2) <= 1
|p_o2| opropEQ(p_o1,p_o2) <= 1

// add this score for every concept equiv (= correspondence) that was generated 
0.5 conceptEQ(e_o1, e_o2)
0.499 dpropEQ(e_o1, e_o2)
0.499 opropEQ(e_o1, e_o2)

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

-0.01 conceptEQIgnored(e_o1, e_o2)

// add the similarity of two words to the objective, if they are set equiv in the solution
sim: !conceptWordSim(w1, w2, sim) v conceptWordEQ(w1,w2)
sim: !dpropWordSim(w1, w2, sim) v dpropWordEQ(w1,w2)
sim: !opropWordSim(w1, w2, sim) v opropWordEQ(w1,w2)

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

// ---------------------------------------------

!conceptWordEQ23aAbCxB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o2) v conceptWordEQ(w1_o1, w1_o2).
!conceptWordEQ23aAbCxB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o2) v conceptWordEQ(w2_o1, w3_o2).

!conceptWordEQ32aAbXcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1) v conceptWordEQ(w1_o1, w1_o2).
!conceptWordEQ32aAbXcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1) v conceptWordEQ(w3_o1, w2_o2).


// ********* relation between words and entities ****************

// *** the rule for matching 0-word entities on 0/1/2/3-word entities (never match this) ***
!concept0Word_o1(e_o1) v !concept0Word_o2(e_o2) v !conceptEQ(e_o1, e_o2).
!dprop0Word_o1(e_o1) v !dprop0Word_o2(e_o2) v !dpropEQ(e_o1, e_o2).
!oprop0Word_o1(e_o1) v !oprop0Word_o2(e_o2) v !opropEQ(e_o1, e_o2).

!concept0Word_o1(e_o1) v !concept1Word_o2(e_o2, x) v !conceptEQ(e_o1, e_o2).
!dprop0Word_o1(e_o1) v !dprop1Word_o2(e_o2, x) v !dpropEQ(e_o1, e_o2).
!oprop0Word_o1(e_o1) v !oprop1Word_o2(e_o2, x) v !opropEQ(e_o1, e_o2).

!concept1Word_o1(e_o1, x) v !concept0Word_o2(e_o2) v !conceptEQ(e_o1, e_o2).
!dprop1Word_o1(e_o1, x) v !dprop0Word_o2(e_o2) v !dpropEQ(e_o1, e_o2).
!oprop1Word_o1(e_o1, x) v !oprop0Word_o2(e_o2) v !opropEQ(e_o1, e_o2).

!concept0Word_o1(e_o1) v !concept2Word_o2(e_o2, x,y) v !conceptEQ(e_o1, e_o2).
!dprop0Word_o1(e_o1) v !dprop2Word_o2(e_o2, x,y) v !dpropEQ(e_o1, e_o2).
!oprop0Word_o1(e_o1) v !oprop2Word_o2(e_o2, x,y) v !opropEQ(e_o1, e_o2).

!concept2Word_o1(e_o1, x,y) v !concept0Word_o2(e_o2) v !conceptEQ(e_o1, e_o2).
!dprop2Word_o1(e_o1, x,y) v !dprop0Word_o2(e_o2) v !dpropEQ(e_o1, e_o2).
!oprop2Word_o1(e_o1, x,y) v !oprop0Word_o2(e_o2) v !opropEQ(e_o1, e_o2).

!concept0Word_o1(e_o1) v !concept3Word_o2(e_o2, x, y, z) v !conceptEQ(e_o1, e_o2).
!dprop0Word_o1(e_o1) v !dprop3Word_o2(e_o2, x, y, z) v !dpropEQ(e_o1, e_o2).
!oprop0Word_o1(e_o1) v !oprop3Word_o2(e_o2, x, y, z) v !opropEQ(e_o1, e_o2).

!concept3Word_o1(e_o1, x, y, z) v !concept0Word_o2(e_o2) v !conceptEQ(e_o1, e_o2).
!dprop3Word_o1(e_o1, x, y, z) v !dprop0Word_o2(e_o2) v !dpropEQ(e_o1, e_o2).
!oprop3Word_o1(e_o1, x, y, z) v !oprop0Word_o2(e_o2) v !opropEQ(e_o1, e_o2).


// *** the rules for matching 1-word entities on 1-word entities ***
!conceptEQ(e_o1, e_o2) v !concept1Word_o1(e_o1, w_o1) v !concept1Word_o2(e_o2, w_o2) v conceptWordEQ(w_o1, w_o2).
!dpropEQ(e_o1, e_o2) v !dprop1Word_o1(e_o1, w_o1) v !dprop1Word_o2(e_o2, w_o2) v dpropWordEQ(w_o1, w_o2).
!opropEQ(e_o1, e_o2) v !oprop1Word_o1(e_o1, w_o1) v !oprop1Word_o2(e_o2, w_o2) v opropWordEQ(w_o1, w_o2).

// *** the rules for matching 2-word entities on 2-word entities ***

!conceptEQ(e_o1, e_o2) v !concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v conceptWordEQ22aAbB(w1_o1, w1_o2, w2_o1, w2_o2) v conceptWordEQ22aBbA(w1_o1, w1_o2, w2_o1, w2_o2).

!dpropEQ(e_o1, e_o2) v !dprop2Word_o1(e_o1, w1_o1, w2_o1) v !dprop2Word_o2(e_o2, w1_o2, w2_o2) v dpropWordEQ(w1_o1, w1_o2).
!dpropEQ(e_o1, e_o2) v !dprop2Word_o1(e_o1, w1_o1, w2_o1) v !dprop2Word_o2(e_o2, w1_o2, w2_o2) v dpropWordEQ(w2_o1, w2_o2).

!opropEQ(e_o1, e_o2) v !oprop2Word_o1(e_o1, w1_o1, w2_o1) v !oprop2Word_o2(e_o2, w1_o2, w2_o2) v opropWordEQ(w1_o1, w1_o2).
!opropEQ(e_o1, e_o2) v !oprop2Word_o1(e_o1, w1_o1, w2_o1) v !oprop2Word_o2(e_o2, w1_o2, w2_o2) v opropWordEQ(w2_o1, w2_o2).

// ** the rules for matching (and not matching) 2-word entities on 1-word entities 
!conceptEQ(e_o1, e_o2) v !concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept1Word_o2(e_o2, w1_o2) v conceptWordEQ(w2_o1, w1_o2).
!conceptEQ(e_o1, e_o2) v !concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept1Word_o2(e_o2, w1_o2) v conceptWordIgnore_o1(w1_o1).
!conceptEQ(e_o1, e_o2) v !concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept1Word_o2(e_o2, w1_o2) v conceptEQIgnored(e_o1, e_o2).

!dprop2Word_o1(e_o1, w1_o1, w2_o1) v !dprop1Word_o2(e_o2, w1_o2) v !dpropEQ(e_o1, e_o2).
!oprop2Word_o1(e_o1, w1_o1, w2_o1) v !oprop1Word_o2(e_o2, w1_o2) v !opropEQ(e_o1, e_o2).

// ** the rules for matching (and not matching) 1-word entities on 2-word entities
!conceptEQ(e_o1, e_o2) v !concept1Word_o1(e_o1, w1_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v conceptWordEQ(w1_o1, w2_o2).
!conceptEQ(e_o1, e_o2) v !concept1Word_o1(e_o1, w1_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v conceptWordIgnore_o2(w1_o2).
!conceptEQ(e_o1, e_o2) v !concept1Word_o1(e_o1, w1_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v conceptEQIgnored(e_o1, e_o2).

!dprop1Word_o1(e_o1, w1_o1) v !dprop2Word_o2(e_o2, w1_o2, w2_o2) v !dpropEQ(e_o1, e_o2).
!oprop1Word_o1(e_o1, w1_o1) v !oprop2Word_o2(e_o2, w1_o2, w2_o2) v !opropEQ(e_o1, e_o2).

// *** the rules for (not) matching 1-word entities on 3-word entities ****
!concept1Word_o1(e_o1, w1_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !conceptEQ(e_o1, e_o2).
!dprop1Word_o1(e_o1, w1_o1) v !dprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !dpropEQ(e_o1, e_o2).
!oprop1Word_o1(e_o1, w1_o1) v !oprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !opropEQ(e_o1, e_o2).

// *** the rules for (not) matching 2-word entities on 3-word entities ****


!conceptEQ(e_o1, e_o2) v !concept2Word_o1(e_o1, w1_o1, w2_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v conceptWordEQ23aAbCxB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o2).

!dprop2Word_o1(e_o1, w1_o1, w2_o1) v !dprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !dpropEQ(e_o1, e_o2).
!oprop2Word_o1(e_o1, w1_o1, w2_o1) v !oprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v !opropEQ(e_o1, e_o2).

// *** the rules for (not) matching 3-word labels on 1-word labels ****
!concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept1Word_o2(e_o2, w1_o2) v !conceptEQ(e_o1, e_o2).
!dprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dprop1Word_o2(e_o2, w1_o2) v !dpropEQ(e_o1, e_o2).
!oprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !oprop1Word_o2(e_o2, w1_o2) v !opropEQ(e_o1, e_o2).

// *** the rules for (not) matching 3-word labels on 2-word labels ****

!conceptEQ(e_o1, e_o2) v !concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept2Word_o2(e_o2, w1_o2, w2_o2) v conceptWordEQ32aAbXcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1).

!dprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dprop2Word_o2(e_o2, w1_o2, w2_o2) v !dpropEQ(e_o1, e_o2).
!oprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !oprop2Word_o2(e_o2, w1_o2, w2_o2) v !opropEQ(e_o1, e_o2).

// *** the rules for matching 3-word labels on 3-word labels ***

!conceptEQ(e_o1, e_o2) v !concept3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !concept3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v conceptWordEQ33aAbBcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aCbAcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aBbCcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aAbCcB(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aCbBcA(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2) v conceptWordEQ33aBbAcC(w1_o1, w1_o2, w2_o1, w2_o2, w3_o1, w3_o2).

!dpropEQ(e_o1, e_o2) v !dprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v dpropWordEQ(w1_o1, w1_o2).
!dpropEQ(e_o1, e_o2) v !dprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v dpropWordEQ(w2_o1, w2_o2).
!dpropEQ(e_o1, e_o2) v !dprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !dprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v dpropWordEQ(w3_o1, w3_o2).

!opropEQ(e_o1, e_o2) v !oprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !oprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v opropWordEQ(w1_o1, w1_o2).
!opropEQ(e_o1, e_o2) v !oprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !oprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v opropWordEQ(w2_o1, w2_o2).
!opropEQ(e_o1, e_o2) v !oprop3Word_o1(e_o1, w1_o1, w2_o1, w3_o1) v !oprop3Word_o2(e_o2, w1_o2, w2_o2, w3_o2) v opropWordEQ(w3_o1, w3_o2).

// *******************************
// ** coherency constraints *****
// *******************************
// assumption: the logic dependencies are already materialized in advance for all relevant predicates

// CONCEPT PATTERN COHERENCY
!conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !sub_o1(c1_o1, c2_o1) v !dis_o2(c1_o2, c2_o2).
!conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !dis_o1(c1_o1, c2_o1) v !sub_o2(c1_o2, c2_o2).

// STABILITY CONSTRAINTS FOR PROPERTY MAPPINGS
// !opropEQ(p_o1, p_o2) v !opropDom_o1(p_o1, c_o1) v !opropDom_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2).
// !opropEQ(p_o1, p_o2) v !opropRan_o1(p_o1, c_o1) v !opropRan_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2).
!dpropEQ(p_o1, p_o2) v !dpropDom_o1(p_o1, c_o1) v !dpropDom_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2).


// STABILITY CONSTRAINTS FOR PROPERTY MAPPINGS (weakend)
!opropEQ(p_o1, p_o2) v !opropDom_o1(p_o1, c_o1) v !opropDom_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2) v conceptSUB(c_o1, c_o2) v conceptSUP(c_o1, c_o2) v conceptXEQ(c_o1, c_o2).
!opropEQ(p_o1, p_o2) v !opropRan_o1(p_o1, c_o1) v !opropRan_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2) v conceptSUB(c_o1, c_o2) v conceptSUP(c_o1, c_o2)  v conceptXEQ(c_o1, c_o2).
// !dpropEQ(p_o1, p_o2) v !dpropDom_o1(p_o1, c_o1) v !dpropDom_o2(p_o2, c_o2) v conceptEQ(c_o1, c_o2) v conceptSUB(c_o1, c_o2) v conceptSUP(c_o1, c_o2)  v conceptXEQ(c_o1, c_o2).


// you can only match bproperties from a pair of inverse properties
// !opropInv_o1(e1_o1, e2_o1) v !opropInv_o2(e1_o2, e2_o2) v !opropEQ(e1_o1, e1_o2) v opropEQ(e2_o1, e2_o2).

// e.g. sub_o1(Person,Author)
// B 1 more general than 2
// P 1 more specific than 2


// forbids SUB od SUP mappings if there is not a subsumption
// relationship under the assumption that an EQ mapping is given
// are these formulae required
!conceptEQ(e1_o1, e_o2) v sub_o1(e1_o1, e2_o1) v !conceptSUP(e2_o1, e_o2).
!conceptEQ(e2_o1, e_o2) v sub_o1(e1_o1, e2_o1) v !conceptSUB(e1_o1, e_o2).
!conceptEQ(e_o1, e1_o2) v sub_o2(e1_o2, e2_o2) v !conceptSUB(e_o1, e2_o2).
!conceptEQ(e_o1, e2_o2) v sub_o2(e1_o2, e2_o2) v !conceptSUP(e_o1, e1_o2).

// enforces the existence of a EQ mapping in the "neighborhood" of a SUB/SUP mapping
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




