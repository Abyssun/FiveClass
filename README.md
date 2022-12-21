# FiveClass

## Basic information of the project

### Project introduction

As a leisure puzzle game, FiveClass has the greatest advantage that its game rules are widely known, easy to use and interesting, so it is favored by the majority of users. You can see the tasks of people playing the game of five sons horizontal in all major platforms providing chess and card games. Grade is not the most important goal in the casual and intelligent play. Through the familiarity with the rules of the game, you can quickly grasp its operation mode. It is also more suitable for the family entertainment of men, women, old and young. Spending time is short. Quick decisions can feel the fun of the game in time and fully enjoy the relaxed and active game process. This kind of entertainment mode can neither help to delay time nor easily adjust the entertainment, which fully suits the entertainment needs of modern people. More importantly, it has developed people's strength and become the most popular game for the young generation. According to statistics, students account for nearly one third of the players in the FiveClass game, which plays a certain role in the healthy growth of students' intelligence. It is the so-called leisure and entertainment.

## Project requirements

FiveClass is one of the competitive events of the National Intelligence Games, and it is a purely strategic chess game for two players. Usually, the two sides use black and white pieces respectively, and the one who forms the line of five pieces at the intersection of the straight line and the horizontal line on the chessboard wins. Specific requirements are as follows:

### Functional requirement

1. Realize the confrontation between people
2. Realize a confrontation between people and machines. Each side of the game holds a piece of chess, and one of them is required to be controlled by a computer
3. The game starts with an empty chessboard
4. Black comes first, white comes next, and only one child can be given at a time
5. The chess pieces are placed on the blank point of the chessboard. After the chess pieces are placed, they cannot move to other points. They cannot be removed from the chessboard or picked up and dropped elsewhere
6. The first chess piece of the black square can be placed on any cross point of the chessboard
7. Subordination by turns is the right of both parties, but either party is allowed to waive the right
8. Until one side wins, end the game
9. There should be a graphical interface with beautiful design and good interactivity
10. Allow repentance
11. You can directly end the ongoing match

### Project operation diagram

![image-20221221093644128](D:\桌面\img\README\image-20221221093644128-1671586610148-32-1671604422365-44.png)

## Requirement analysis

### Functional requirements analysis

The system interface is required to be simple and easy to operate, reflecting the most basic rules in the FiveClass game rules, such as "draw", "restart", etc. FiveClass is also characterized by quick decision. The computer should make a judgment in a short time and provide some help. It should immediately point out when it violates the rules of the game.

### System data structure analysis

The FiveClass chessboard is a 15 * 15 chessboard. For chess games, a two-dimensional array of the same size is used to store the current pattern on the chessboard. Use a two-dimensional array of the same size to store the weight calculated by each point on the chessboard. In order to calculate and implement the regretful chess operation, a stack is defined to record the chess pieces in each step. These three data structures are defined in ChessPad and named ColorPad, ValuePad and Stack respectively. ChessPad is equivalent to a virtual chessboard, and the system interface is to draw a chessboard according to the above three data of this type to form a visual interface.
In order to realize human-computer interaction, we have defined an AI class in the project to control the computer's calculation of the position of the falling object and the execution of the falling action.
The definition of AI class on the machine side. The data structure used in this class has two Maps, namely AttackMap, in which the attack chess type and the corresponding weight of the chess type are stored; DefendMap stores defensive chess types and their corresponding weights. In these two Maps, the chess type is key and the weight value is value.

### System interface analysis

The system interface is arranged up and down. The upper part is a chessboard with a size of 800 * 700 pixels. The chessboard is a separate container defined as ChessPadPanel; The lower part is the button area, which is also a separate container ButtonPanel. Each button is used to operate various functions of the system.
MainPanel on the main page contains two components, ChessPadPanel and ButtonPanel. In order to achieve the top and bottom layout, MainPanel needs to inherit the JSplitPanel class to achieve the top and bottom layout of containers.

### Analysis of AI's Chess Decision

The AI game decision of this system adopts a backward looking mechanism, that is, before playing chess, AI will assume what kind of chess type each point will get after playing black chess and white chess at each empty point on the chessboard, and then calculate the sum of the weights of each point as the weight value of the point. Finally, select a point with the largest weight in the ValuePad as the next step of AI.

###Analysis of system button functions

Mode button, the system has two modes, namely "everyone versus game" and "man-machine versus game", which are used to select a button and click it to enter the corresponding game mode. The chess system needs to judge the current game mode for each step in the whole game process, and then choose whether to call AI to play chess. Therefore, a mode parameter is defined in ChessPad, This parameter is used to record the current game mode.
End button: this button can end the current game. At the same time, the chess game of the previous game cannot affect the game of the next game after the end. Therefore, when you click the end button, you need to clear the data on the chessboard. ColorPad and ValuePad should both be reset to an empty chessboard.
Skip button is used to enable the user to abandon the chess (that is, to abandon the current round of chess playing). When clicking this button, the current chess player's chess playing power will be handed over to the opponent. In order to realize this function, the system needs to be able to judge whether the current chess player is a white or black player. Therefore, a BlackTurn parameter is defined in ChessPad, which is a Boolean value. When the value is true, it means the current black round, If the value is false, it means that the current round is a white round, so to realize the skip function, you only need to modify the BalckTurn value each time the user clicks the button.
The Repentance button is used to realize the user's repentance operation. It is necessary to record the chess playing order of the chess game. Therefore, the system has a stack stack to record every move of the current chess game. The user clicks Repentance to remove the moves at the top of the stack from the stack and redraw the chess board.

## System Design Summary

![流程图](D:\桌面\img\README\流程图-1671593241994-37.jpg)

## Coding and implementation

###Project directory structure

![image-20221221111954906](D:\桌面\img\README\image-20221221111954906-1671592796891-34.png)