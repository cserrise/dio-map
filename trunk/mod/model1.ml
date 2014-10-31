
// ***** predicate definition *****

// observed
*concept1hasLabel1(Concept1, Word1)
*concept1hasLabel2(Concept1, Word1, Word1)
*concept1hasLabel3(Concept1, Word1, Word1, Word1)
*concept1hasLabel4(Concerp1, Word1, Word1, Word1, Word1)


*concept2hasLabel1(Concept2, Word2)
*concept2hasLabel2(Concept2, Word2, Word2)
*concept2hasLabel3(Concept2, Word2, Word2, Word2)
*concept2hasLabel4(Concerp2, Word2, Word2, Word2, Word2)

*wordSim(Word1, Word2, float_)

// hidden
wordEquiv(Word1, Word2)
conceptEquiv(Concept1, Concept2)


// ***** program ******

sim: !wordSim(w1, w2, sim) v wordEquiv(w1,w2)

// one-word labeled concepts
!concept1hasLabel1(c1, w1p1) v !concept2hasLabel1(c2, w2p1) v !wordEquiv(w1p1,w2p1) v conceptEquiv(c1,c2).

// two-word labeled concepts
!concept1hasLabel2(c1, w1p1, w1p2) v !concept2hasLabel2(c2, w2p1, w2p2) v !wordEquiv(w1p1,w2p1) v !wordEquiv(w1p2,w2p2) v conceptEquiv(c1,c2).

// three-word labeled concepts
!concept1hasLabel3(c1, w1p1, w1p2, w1p3) v !concept2hasLabel3(c2, w2p1, w2p2, w2p3) v !wordEquiv(w1p1,w2p1) v !wordEquiv(w1p2,w2p2) v !wordEquiv(w1p3,w2p3) v conceptEquiv(c1,c2).


// four-word labeled concepts
!concept1hasLabel4(c1, w1p1, w1p2, w1p3, w1p4) v !concept2hasLabel4(c2, w2p1, w2p2, w2p3, w2p4) v !wordEquiv(w1p1,w2p1) v !wordEquiv(w1p2,w2p2) v !wordEquiv(w1p3,w2p3) v !wordEquiv(w1p4,w2p4) v conceptEquiv(c1,c2).


|c1| conceptEquiv(c1,c2) <= 1
|c2| conceptEquiv(c1,c2) <= 1

