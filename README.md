# Sudoku Solver

Command-line program which demonstrates a variety of solutions to solving both regular and 'killer' sudoku puzzles.
Program will take a sudoku grid as an input file and either print or output the solved puzzle to a file of your choice.

## Key Files

Three solving methods are available for regular sudoku:
- [Backtracking](solver/BackTrackingSolver.java)
- Knuth's Algorithm X (solver/AlgorXSolver.java)
- Knuth's Dancing Links (solver/DancingLinksSolver.java)

Two solvers are available for 'killer' sudoku puzzles:
- Backtracking (solver/KillerBackTrackingSolver.java)
- Dancing Links (solver/KillerAdvancedSolver.java)

Dancing Links data structure can be found in the /dancingLinks 
directory.

This program was created for RMIT's Algorithms and Analysis Subject and contains skeleton code provided by the university.
Advanced solutions impliment those outlined in David Knuth's 2000 paper 'Dancing Links'.
