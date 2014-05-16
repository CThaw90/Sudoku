Sudoku Puzzle Solver
============================

Build Scripts using ant
-------------------------
1. Main directory /Sudoku
2. ant compile -- compiles program
3. ant jar -- creates jar file in dist folder
4. ant run -- runs program using ant build script

Running program using jar file
---------------------------
1. Sub Directory /Sudoku/dist
2. java -jar Sudoku.jar 

Sudoku test files
---------------------------
1. There are 15 files located in Sub Directory /Sudoku/dist/SudokuBoards
2. 3 files contain easy Sudoku puzzles.
3. 3 files contain intermediate Sudoku Puzzles.
4. 3 files contain hard Sudoku Puzzles.
5. 6 files are fail test files and are designed not to be solvable
	A. BadFormatFiles -- These files contain formats that violates
	   the Sudoku Solvers parser mandates
	B. DummyTextFiles -- The Sudoku Solver is designed to look 
	   for files that carry the .sudoku extension in order to 
	   differentiate between other files.
6. The Sudoku solver is hard coded to look for the directory /SudokuBoards
   by default 

Sudoku Board Sizes
--------------------------
The Sudoku Solver supports 4 different Board Sizes:
	SMALL: 4x4 
	REGULAR: 9x9
	LARGE: 16x16
	EXTRA LARGE: 25x25

Loading Custom SudokuBoards
----------------------------
The Sudoku Solver (CSV) format is described with the following syntax:

	Declare the size of the board at the beginning of the file
	Size syntax: 9x9:

	Add a colon after the declaration of the board size

Separate each field value on the board with a comma.
Values are populated horizontally first:

	1,6,8,3,9
	
Represent the values at 1=(0,0) 6=(0,1) 8=(0,2) 3=(0,3) 9=(0,4)

Blank field values are represented by the '*' symbol:

Sudoku Board example:

	example.sudoku			rendered Sudoku Board
 
	9x9:				
	*,8,*, *,*,*, *,6,*,		+-----------------------+
	3,*,*, *,1,*, *,*,9,		| * 8 * | * * * | * 6 * |
	1,*,7, *,6,*, 8,*,5,		| 3 * * | * 1 * | * * 9 |
					| 1 * 7 | * 6 * | 8 * 5 |
	*,*,1, 7,*,2, 4,*,*,		|-------+-------+-------|
	2,*,*, *,*,*, *,*,8,		| * * 1 | 7 * 2 | 4 * * |
	*,*,4, 1,*,8, 3,*,*,		| 2 * * | * * * | * * 8 |
					| * * 4 | 1 * 8 | 3 * * |
	6,*,9, *,2,*, 5,*,1,		|-------+-------+-------|
	4,*,*, *,5,*, *,*,2,		| 6 * 9 | * 2 * | 5 * 1 |
	*,5,*, *,*,*, *,3,*		| 4 * * | * 5 * | * * 2 |
		 			| * 5 * | * * * | * 3 * |
					+-----------------------+

2. Load your custom made Sudoku Board by specifing the file path using the
   --file argument

   java -jar Sudoku --file "path_to_file"

   You can also load multiple files into the Sudoku Solver by specifying
   a folder location using the --folder argumet

   java -jar Sudoku --folder "path_to_folder"

   Be sure to use the .sudoku extension

