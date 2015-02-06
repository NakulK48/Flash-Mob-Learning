package uk.ac.cam.grpproj.lima.flashmoblearning;

/** Package containing the core logic. Just above the database backend layer, 
 * and relatively tightly coupled to it, in that they both call each other
 * extensively via their public APIs.
 * 
 * Implements documents, tags and users, and operations such as forking a new
 * document.
 * 
 * Objects will be fetched from the database within a single thread, with 
 * consistency enforced at the transaction level, so there is no need for
 * synchronization on objects here.
 */