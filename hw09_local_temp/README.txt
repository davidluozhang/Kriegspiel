=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays *********

  I represented the chessboard as a 2D array of Piece objects.

  I avoid issues like ArrayIndexOutOfBounds by carefully controlling what
  inputs can be passed in by the user. Inputs are taken as a Position object,
  which is already checked in GameBoard for the most obvious issues like it
  being null. Before I do anything with my array, I call inBoundsPosition()
  to ensure the position's row and col fields are both within the legal range
  of 0 to 7. In places where I manually set the iteration ranges, for example
  in each Piece's isLegalMove() and pathMove() method, I was careful that the
  values are in range for every for loop. With Bishop and Queen, I made sure to
  put in stop conditions because it's easy for one value to go out of bounds
  when moving diagonally.

  I only used one 2D array to represent the entire board.

  The array is sufficiently encapsulated. It is private without a setBoard()
  method. I also made my getBoard() return a deep copy of the board, such
  that all the Piece objects in the returned Piece[][] array are deep copies
  as well. This way, I cannot indirectly modify the contents of the board
  by modifying a Piece. The only way to update the 2D array is through the
  playTurn() method or by calling reset() (which still only creates a new
  board without modifying the old one).

  For representing a grid-based game where at most one thing can be at one
  place, a 2D array is the most intuitive and easiest to visualize
  implementation.

  I made a 2D array of Pieces to take advantage of dynamic dispatch. I could
  store any subclass of Piece, like Kings, Queens, Pawns, etc. in the array.
  I could then call an instance method on the Piece object, and via dynamic
  dispatch, use the implementation specific to the subtype that overrides the
  superclass's. This got rid of the need to, say, store a 2D array of Strings
  or ints and then trudge through matching values to behavior via if-statements.

  2. Inheritance and Subtyping *********

  The program interacts with Pieces in largely the same ways (moving,
  displaying) considering roughly the same attributes (eg. id, name, position).
  Simultaneously, each piece has its own unique way of doing those things eg.
  unique movement patterns, attack/defense strengths, appearances.

  Some implementation is shared, eg. checking that the piece belongs to the
  active player in isLegalMove(), which goes into the Piece superclass.
  Distinct implementations, like each piece's movement logic in isLegalMove()
  and pathMove(), are situated in methods in each dynamic subclass that override
  the superclass's methods. Some pieces don't have extra rules and thus don't
  need to override, eg. Knight doesn't override pathMove(), as the Knight can
  jump over pieces.

  All subclasses instances should inherit and easily access the fields declared
  in the superclass like name and ID, so I set these fields to private with
  public getters but protected setters to preserve encapsulation as best I can.

  As discussed in the 2D Array section, I called overridden methods on
  objects in a 2D Piece array that were of static type Piece but dynamic type
  King, Queen, Pawn, etc. such that the behavior was tailored to the specific
  subtype.

  3. Collections *********

  I implemented unlimited undo functionality using a LinkedList called
  previousStates in GameBoard that held onto previous Kriegspiel game states by
  saving a deep copy of the pre-move game state with each move.

  I originally re-implemented LinkedList by having each Kriegspiel object
  keep track of the previous Kriegspiel object, but Aarushi told me it was
  better to use a LinkedList in GameBoard instead, as the dynamic resizing
  was more clearly seen.

  I used a LinkedList because order matters, repeated game states are allowed
  (eg. by moving the same piece back and forth), and because I have no use for
  unique keys. To undo, I simply assigned the previous game state Kriegspiel
  object to the current game state. I added new copys to the front with
  addFirst(), lengthening the LinkedList by 1, and undid by removing and
  returning from the front with remove(), shortening the LinkedList by 1,
  thereby doing dynamic resizing. I didn't have to iterate repeatedly through
  the LinkedList, as what I was effectively doing was popping from a stack with
  each undo.

  The LinkedList previousStates was encapsulated because it was private with no
  setter and only manipulatable through my defined undo and reset methods and
  the logic nestled within the actionListener. For both 2D arrays and
  collections, I made sure with multiple TAs that my game model was
  sufficiently encapsulated from the outside world.

  I model part of the game state through the activePieces and takenPieces
  HashSets in my Kriegspiel class that keep track of active and taken pieces.
  They let me easily display the visible pieces and check if the game is over.
  There is some overlap with the 2D array concept, but this is by design.
  By preserving the invariant that the Pieces in the activePieces HashSet are
  reference equal to the corresponding pieces in the 2D array, I get both the
  arithmetic convenience of 2D arrays (eg. indices for path calculations) and
  the speed and ease of the hashset (eg. for quick checks and displaying without
  traversing the entirety of a mostly-null board). I emailed my TAs, and they
  agreed that this dual implementation met the HW's stipulated requirements.

  I don't hold my TakenPieces HashSet to the same reference equal invariant
  because there is no longer a piece on the board to be reference equal to.
  I only use it to check if the game's over, and the overriding equals()
  method in Piece only checks for equal id. Further, once a piece is taken,
  the program never changes it.

  I chose HashSets because order doesn't matter, there shouldn't be any repeats,
  and I have little use for the unique keys, as I could easily get the pieces
  from the 2D array of Pieces via passed-in Position objects or indexes. I use
  HashSets over TreeSets because they're faster and because there is no
  "sorting" to pieces, and TreeSets are a sorted set while HashSets aren't.
  I originally thought about mapping a unique String or int key to each Piece,
  but I decided against it because it's better practice to make the unique
  identifier a core attribute (ie. a unique id field) of the object.

  4. JUnit Testable Component *********

  I used JUnit tests to test methods independently of the GUI, including by
  simulating entire games of chess to test my gameplay mechanics.

  No tests test the same thing, but some necessarily involve multiple methods
  by the nature of chess (eg. to test attacking, you must first move the pieces
  into an attacking position.) Many are testing the piece-specific
  implementations of isLegalMove(), pathMove() methods, and other edge cases
  like en passant, castling, and countless varieties of illegal moves.

  I designed my code to be unit-testable. I split the pre-turn actions in
  playTurn() into discrete components. There are different methods checking
  legality, finding paths, etc. that work independently of each other before
  playTurn() finally updates the game state.

  5. (EXTRA / ALTERNATIVE) Complex Game Logic *********

  I spoke with Aarushi about my project, and she indicated that it would get
  credit for complex game logic. It incorporates many of chess's special moves
  like en passant, castling, and pawn promotion. Where it doesn't have check or
  checkmate, it has random attack mechanisms (a win probability calculated by
  each piece's strength attribute), path-finding (to implement the interception
  mechanic), and a visibility element to simulate the fog of war during and
  between turns.



=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  The Piece superclass provides a framework for how pieces behave on the board
  and some commonalities:
  - instance variables like name and id
  - some shared implementation of methods like isLegalMove() and getters/setters
  - a static counter variable to initialize each Piece with a unique ID. The
    uniqueness of id is an important invariant, as it is how I identify if two
    pieces are equal to each other. I preserve the unique ID invariant in the
    overriding copy() methods by manually setting the deep copy's id to the
    original piece's.
  - overrides of hashCode() and equals()

  Dynamic subclasses for each Piece type like Pawn and Rook then extend the
  Piece superclass, providing their distinct implementations of methods while
  using the superclass methods and fields when necessary.

  For example, all isLegalMove() methods in the subclasses begin by calling the
  isLegalMove() method in the Piece class to check for the illegalMoves criteria
  shared by all pieces (eg, out of bounds, not your color, move-to space
  occupied by your own piece). They then check against the subclass's unique
  movement pattern.

  Some subclasses have their own private instance fields to assist in their
  unique implementations. For example, pawns have to keep track of their
  previous position for checking en passant, and kings have to keep track
  of if they can castle or not based on if the king has moved or not.

  As discussed above in the inheritance and subtyping section, this class
  hierarchy made manipulating the game state very straightforward, as I could
  call the same methods on objects statically typed as Pieces via the 2D array
  board and do behavior specific to that Piece's dynamic type.

  The Kriegspiel class

  The Kriegspiel class houses the game model and methods for modifying the game
  model. Its fields represent parts of the game state, including a 2D array of
  pieces representing the board, a boolean representing the player turn, sets
  of active and taken Pieces, the last Piece moved, and the identifying IDs of
  the Kings.

  The Position class

  A convenient way of storing each Piece's coordinates on the board. We can
  convert from Position to Chess algebraic notation and vice versa. Via the
  strict vetting of user inputs and checks within the program's internal
  logic, Position objects have the invariant of being bounded between 0 and 8
  for its row and col fields.

  The GameBoard class

  The GameBoard class connects the game model (stored as an instance variable
  of a GameBoard object) to the user by displaying the game model, parsing user
  inputs like clicks, updating the model accordingly by activating playTurn(),
  undo(), or reset(), and then re-displaying the updated model. The model was
  displayed according to certain criteria derived from the Game State itself,
  eg painting pieces and broadcasting a winner, and fields in the GameBoard
  class, like the turn of the game and the currently chosen piece. It allowed
  me to easily follow the MVC design pattern and to be strict on what inputs
  I allow into my backend, warding off bad inputs that could yield misbehaving
  code.

  The key invariant in recording the game model through the progression of the
  game is that the Pieces contained in activePieces and board are reference
  equal within each game model, but activePieces, board, and the Pieces
  are not reference equal between game models. This allows me to perform
  operations on shared states (each Piece) during each turn while not allowing
  future operations to change past saved game states eg. changing activePieces.

  The RunKriegspiel class

  The RunKriegspiel class sets up the game, readying all the UI/UX components
  necessary to display the game and instructions and take in user input.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  I am very grateful that I fleshed out the design of the program on paper
  before writing out hundreds of lines of code, as it allowed me to write the
  bulk of the game without many flaws. I am also glad that I was constantly
  JUnit testing along the way, as I caught mistakes early.

  The hardest part in this stage was catching all the small errors in things
  like for loop conditions and accidental reference copies instead of deep
  copies that allowed Objects to be modified elsewhere unintentionally.

  I did struggle towards the end, as I had not intended on implementing the
  undo functionality. I went through multiple design iterations, at one point
  accidentally re-inventing the LinkedList by having each Kriegspiel object
  store a copy of the previous Kriegspiel object, and spent a lot of time
  debugging to make sure all the right objects were or weren't reference equal.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

  If I were to do it again, I would try to split up the playTurn() method even
  further, perhaps into more unit-testable methods like an attack method or a
  generateWinProbability method. As is, however, there is good separation of
  functionality.

  The private state is well encapsulated, but I would also make the
  encapsulation stronger between classes. It was a pain to track all the places
  where changes could occur as functions called each other and set other piece's
  fields. This doesn't affect encapsulation to the end user, but would
  certainly make my life easier.

  I could also get around this by ditching the hashSet of active pieces
  doing the logic and drawing entirely via the 2D Piece array, as preserving
  the invariant of reference equality within game state was very laborious in
  a program with so much manipulation and copying. Getting rid of this would
  decrease the number of places I'd have to debug.

  There are also some redundant operations in the code, eg checking in the
  event handler and also the isLegalMove() if the selected Piece belongs to
  the active player's. I kept them just because they didn't hurt, but I could
  probably streamline the code further without affecting edge case performance.




========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  Information and rules for the original Kriegspiel:
  https://en.wikipedia.org/wiki/Kriegsspiel

  Images for my pieces:
  https://www.pngegg.com/en/png-bsylj
