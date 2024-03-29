// ****************************************
// *** BETA: concept <-> label <-> word ***
// ****************************************


// ********************************
// ***** predicate definition *****
// ********************************

// *** observed ***

// words, labels, concepts

*conceptHasLabel_o1(Concept1, Label1)
*opropHasLabel_o1(OProp1, Label1)
*dpropHasLabel_o1(DProp1, Label1)

*conceptHasLabel_o2(Concept2, Label2)
*opropHasLabel_o2(OProp2, Label2)
*dpropHasLabel_o2(DProp2, Label2)

*has1Word_o1(Label1, Word1)
*has2Word_o1(Label1, Word1, Word1)
*has3Word_o1(Label1, Word1, Word1, Word1)

*has1Word_o2(Label2, Word2)
*has2Word_o2(Label2, Word2, Word2)
*has3Word_o2(Label2, Word2, Word2, Word2)

*wordSim(Word1, Word2, float_)

// semantics
sub_o1(Concept1, Concept1)
dis_o1(Concept1, Concept1)
sub_o2(Concept2, Concept2)
dis_o2(Concept2, Concept2)

dpropDom_o1(DProp1, Concept1)
opropDom_o1(OProp1, Concept1)
opropRan_o1(OProp1, Concept1)

dpropDom_o2(DProp2, Concept2)
opropDom_o2(OProp2, Concept2)
opropRan_o2(OProp2, Concept2)


// *** hidden ***
wordEQ(Word1, Word2)
labelEQ(Label1, Label2)

conceptEQ(Concept1, Concept2)
opropEQ(OProp1, OProp2)
dpropEQ(DProp1, DProp2)

blindWord_o1(Word1)
blindWord_o2(Word2)


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
// IS IT NEEDED? Its seems so ...
0.2 conceptEQ(c1, c2)
0.2 opropEQ(o1, o2)
0.2 dpropEQ(d1, d2)

// blind words should not occur, setting a word blind has to be punished
-0.3 blindWord_o1(w1)
-0.3 blindWord_o2(w2)

// add the similarity of two words to the objectice, if they are set equiv in the solution
sim: !wordSim(w1, w2, sim) v wordEQ(w1,w2)

// if two labels are equiv the concepts, dataprops, objectprops that they are attached to are equiv
!labelEQ(l_o1, l_o2) v !conceptHasLabel_o1(c_o1, l_o1) v !conceptHasLabel_o2(c_o2, l_o2) v conceptEQ(c_o1, c_o2).
// !labelEQ(l_o1, l_o2) v !opropHasLabel_o1(o_o1, l_o1) v !opropHasLabel_o2(o_o2, l_o2) v opropEQ(o_o1, o_o2).
// !labelEQ(l_o1, l_o2) v !dpropHasLabel_o1(d_o1, l_o1) v !dpropHasLabel_o2(d_o2, l_o2) v dpropEQ(d_o1, d_o2).

// CONTINUE HERE


// if two concepts are equiv then there exists (important) a pair of lables of these concepts that are equiv
// ... this cannot be expressed by a general formula ... groundings for this are added automatically
!conceptEQ(c_o1, c_o2) v !conceptHasLabel_o1(c_o1, l_o1) v !conceptHasLabel_o2(c_o2, l_o2) v labelEQ(l_o1, l_o2).
!opropEQ(o_o1, o_o2) v !opropHasLabel_o1(o_o1, l_o1) v !opropHasLabel_o2(o_o2, l_o2) v labelEQ(l_o1, l_o2).
!dpropEQ(d_o1, d_o2) v !dpropHasLabel_o1(d_o1, l_o1) v !dpropHasLabel_o2(d_o2, l_o2) v labelEQ(l_o1, l_o2).


// CRITICAL
// is it added automatically already?




// *** the rules for matching 1-word labels on 1-word labels ***
// CONCEPTS
!has1Word_o1(l1_o1, w1_o1) v !has1Word_o2(l1_o2, w1_o2) v !wordEQ(w1_o1, w1_o2) v labelEQ(l1_o1, l1_o2).
!labelEQ(l1_o1, l1_o2) v !has1Word_o1(l1_o1, w1_o1) v !has1Word_o2(l1_o2, w1_o2) v wordEQ(w1_o1, w1_o2).

// *** the rules for matching 2-word labels on 2-word labels ***
!has2Word_o1(l1_o1, w1_o1, w2_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v !wordEQ(w1_o1, w1_o2) v !wordEQ(w2_o1, w2_o2) v labelEQ(l1_o1, l1_o2).
!labelEQ(l1_o1, l1_o2) v !has2Word_o1(l1_o1, w1_o1, w2_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v wordEQ(w1_o1, w1_o2).
!labelEQ(l1_o1, l1_o2) v !has2Word_o1(l1_o1, w1_o1, w2_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v wordEQ(w2_o1, w2_o2).

// ** the rules for matching 2-word labels on 1-word labels in case of a blind word
!has2Word_o1(l1_o1, w1_o1, w2_o1) v !has1Word_o2(l1_o2, w1_o2) v !wordEQ(w2_o1, w1_o2) v !blindWord_o1(w1_o1) v labelEQ(l1_o1, l1_o2).
!labelEQ(l1_o1, l1_o2) v !has2Word_o1(l1_o1, w1_o1, w2_o1) v !has1Word_o2(l1_o2, w1_o2) v wordEQ(w2_o1, w1_o2).
!labelEQ(l1_o1, l1_o2) v !has2Word_o1(l1_o1, w1_o1, w2_o1) v !has1Word_o2(l1_o2, w1_o2) v blindWord_o1(w1_o1).

// ** the rules for matching 1-word labels on 2-word labels in case of a blind word
!has1Word_o1(l1_o1, w1_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v !wordEQ(w1_o1, w2_o2) v !blindWord_o2(w1_o2) v labelEQ(l1_o1, l1_o2).
!labelEQ(l1_o1, l1_o2) v !has1Word_o1(l1_o1, w1_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v wordEQ(w1_o1, w2_o2).
!labelEQ(l1_o1, l1_o2) v !has1Word_o1(l1_o1, w1_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v blindWord_o2(w2_o1).

// *** the rules for matching 2-word labels on 2-word labels ***
!has2Word_o1(l1_o1, w1_o1, w2_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v !wordEQ(w1_o1, w1_o2) v !wordEQ(w2_o1, w2_o2) v labelEQ(l1_o1, l1_o2).
!labelEQ(l1_o1, l1_o2) v !has2Word_o1(l1_o1, w1_o1, w2_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v wordEQ(w1_o1, w1_o2).
!labelEQ(l1_o1, l1_o2) v !has2Word_o1(l1_o1, w1_o1, w2_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v wordEQ(w2_o1, w2_o2).

// *** the rules for (not) matching 1-word labels on 3-word labels ****
!has1Word_o1(l1_o1, w1_o1) v !has3Word_o2(l1_o2, w1_o2, w2_o2, w3_o2) v !labelEQ(l1_o1, l1_o2).

// *** the rules for (not) matching 2-word labels on 3-word labels ****
!has2Word_o1(l1_o1, w1_o1, w2_o1) v !has3Word_o2(l1_o2, w1_o2, w2_o2, w3_o2) v !labelEQ(l1_o1, l1_o2).

// *** the rules for (not) matching 3-word labels on 1-word labels ****
!has3Word_o1(l1_o1, w1_o1, w2_o1, w3_o1) v !has1Word_o2(l1_o2, w1_o2) v !labelEQ(l1_o1, l1_o2).

// *** the rules for (not) matching 3-word labels on 2-word labels ****
!has3Word_o1(l1_o1, w1_o1, w2_o1, w3_o1) v !has2Word_o2(l1_o2, w1_o2, w2_o2) v !labelEQ(l1_o1, l1_o2).


// *** the rules for matching 3-word labels on 3-word labels ***
!has3Word_o1(l1_o1, w1_o1, w2_o1, w3_o1) v !has3Word_o2(l1_o2, w1_o2, w2_o2, w3_o2) v !wordEQ(w1_o1, w1_o2) v !wordEQ(w2_o1, w2_o2) v !wordEQ(w3_o1, w3_o2) v labelEQ(l1_o1, l1_o2).
!labelEQ(l1_o1, l1_o2) v !has3Word_o1(l1_o1, w1_o1, w2_o1, w3_o1) v !has3Word_o2(l1_o2, w1_o2, w2_o2, w3_o2) v wordEQ(w1_o1, w1_o2).
!labelEQ(l1_o1, l1_o2) v !has3Word_o1(l1_o1, w1_o1, w2_o1, w3_o1) v !has3Word_o2(l1_o2, w1_o2, w2_o2, w3_o2) v wordEQ(w2_o1, w2_o2).
!labelEQ(l1_o1, l1_o2) v !has3Word_o1(l1_o1, w1_o1, w2_o1, w3_o1) v !has3Word_o2(l1_o2, w1_o2, w2_o2, w3_o2) v wordEQ(w3_o1, w3_o2).


// *******************************
// ** coherency constraints *****
// *******************************

//!conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !sub_o1(c1_o1, c2_o1) v !dis_o2(c1_o2, c2_o2).
//!conceptEQ(c1_o1, c1_o2) v !conceptEQ(c2_o1, c2_o2) v !dis_o1(c1_o1, c2_o1) v !sub_o2(c1_o2, c2_o2).

//!sub_o1(c1_o1, c2_o1) v !sub_o1(c2_o1, c3_o1) v sub_o1(c1_o1, c3_o1).
//!sub_o2(c1_o2, c2_o2) v !sub_o2(c2_o2, c3_o2) v sub_o2(c1_o2, c3_o2).

//!sub_o1(c1_o1, c2_o1) v !dis_o1(c1_o1, c3_o1)  v dis_o1(c2_o1, c3_o1).
//!sub_o2(c1_o2, c2_o2) v !dis_o2(c1_o2, c3_o2)  v dis_o2(c2_o2, c3_o2).






