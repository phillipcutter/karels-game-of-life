public class SuperKarelProgram extends SuperKarel
{

// Web reference - https://bitstorm.org/gameoflife/
// Rules from https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life

// Execution Description
//
// The program can be summarized by two main parts:
// Evaluation and Execution
//
// The program starts by going to a point and calculating it's neighbors.
// If it survives it sets the count 2, if it dies it's set to 3.
// The reason for this is to make sure the execution is not influenced by
// Karel's movement through the board reading from bottom right to 
// top left.
// It does this for each point until it resets it's position to the bottom left
//
// Now it's time to kill.
//
// Karel will now look at each ball and remove all balls if it's 4 therefore
// killing it. Otherwise if it's 3 it will remove 1 ball and continue.
// Cells that are meant to be revived will have 2 balls which will tell the
// neighbor counter to ignore that cell.
//
// This process repeats for each frame

// 2 = Revive
// 3 = Live
// 4 = Die

// O is cell, X is neighbor
// XXX
// XOX
// XXX

// Four different rules
// 1. Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
// 2. Any live cell with two or three live neighbors lives on to the next generation.
// 3. Any live cell with more than three live neighbors dies, as if by overpopulation.
// 4. Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
    
    public void run()
    {
        // for (int i = 0; i < 48; i++) {
        while (frontIsClear()) {
            action();
            turnLogic();
        }
    }
    
    private void turnLogic() 
    {
        while(frontIsClear())
            {
                move();
                action();
            }
            if (facingWest())
            {
                if (rightIsBlocked())
                {
                    // At top left
                    turnLeft();
                    moveToEnd();
                    turnLeft();
                    // At bottom left
                    // Finished one cycle
                    executeGrid();
                }
                else
                {
                    turnRight();
                    move();
                    turnRight();
                }
            }
            else
            {
                while(notFacingNorth())
                {
                    turnLeft();
                }
                move();
                if (leftIsClear())
                {
                    turnLeft();
                }
                else {
                    turnRight();
                }
            }
    }
    
    private void scanForBall()
    {
        while (noBallsPresent())
        {
            if (frontIsClear())
            {
                move();
            }
            else
            {
                putBall();
                putBall();
            }
        }
        takeBall();
        if (ballsPresent())
        {
            takeBall();
        }
        else
        {
            putBall();
        }
    }
    
    private void evalSpace()
    {
        if (ballsPresent())
        {
            // 1 Ball
            takeBall();
            if (ballsPresent())
            {
                // 2 Balls
                takeBall();
                if (ballsPresent())
                {
                    // 3 Balls
                    takeBall();
                    if (ballsPresent())
                    {
                        // 4 Balls
                        takeBall();
                        if (ballsPresent())
                        {
                            // 5 Balls? Should never be the case
                        }
                        else
                        {
                            // Kill cell, do nothing
                        }
                    }
                    else
                    {
                        // Keep cell alive, place ball
                        putBall();
                    }
                }
                else
                {
                    // Revive cell, place ball
                    putBall();
                }
            }
        }
    }
    
    private void executeGrid()
    {
        evalSpace();
        while(notFacingSouth())
        {
            while(frontIsClear())
            {
                move();
                evalSpace();
            }
            if (facingWest())
            {
                if (rightIsBlocked())
                {
                    // At top left
                    turnLeft();
                    moveToEnd();
                    turnLeft();
                    // At bottom left
                    // Finished
                    turnRight();
                }
                else
                {
                    turnRight();
                    move();
                    turnRight();
                }
            }
            else
            {
                while(notFacingNorth())
                {
                    turnLeft();
                }
                move();
                if (leftIsClear())
                {
                    turnLeft();
                }
                else
                {
                    turnRight();
                }
            }
        }
        turnLeft();
    }
    
    private void action()
    {
        if (ballsPresent())
        {
            actionOnCell();
        }
        else
        {
            actionOnDead();
        }
    }
    
    private void actionOnDead()
    {
        // First lets add the amount of neighbors to the cell
        computeNeighbors();
        // The amount of neighbors is now equal to the number of balls in the space
        if (ballsPresent())
        {
            // 1 Neighbor
            takeBall();
            if (ballsPresent())
            {
                // 2 Neighbors
                takeBall();
                if (ballsPresent())
                {
                    // 3 Neighbors
                    takeBall();
                    if (noBallsPresent())
                    {
                        setRevive(); // Only revive cell if there are exactly -
                        // 3 neighbors
                    }
                    else
                    {
                        while (ballsPresent())
                        {
                            takeBall();
                        }
                    }
                }
            }
        }
    }
    
    private void actionOnCell()
    {
        // First lets add the amount of neighbors to the cell
        computeNeighbors();
        // Now the amount of balls in the cell minus 1 is the amount of neighbors
        takeBall(); // Remove the first one which represents the cell
        // 0 Neighbors
        if (ballsPresent())
        {
            // 1 Neighbor
            takeBall();
            if (ballsPresent())
            {
                // 2 Neighbors
                takeBall();
                if (ballsPresent())
                {
                    // 3 Neighbors
                    takeBall();
                    if (ballsPresent())
                    {
                        // 4 Neighbors
                        // This cell must die, it has too many neighbors
                        while (ballsPresent())
                        { 
                            // There may be more than 4 Neighbors (Up to 8)
                            takeBall();
                        }
                        setKill();
                    }
                    else
                    {
                        setLive(); // 3, Cell should stay alive
                    }
                }
                else
                {
                    setLive(); // 2, Cell should stay alive
                }
            }
            else
            {
                // 1, If there are no balls left then the cell is dead
                setKill();
            }
            
        }
        else
        {
            // 0 Neighbors, If there are no balls left then the cell is dead
            setKill();
        }
    }
    
    private void setRevive()
    {
        putBall();
        putBall();
    }
    
    private void setLive()
    {
        putBall();
        putBall();
        putBall();
    }
    
    private void setKill()
    {
        putBall();
        putBall();
        putBall();
        putBall();
    }
    
    private void computeNeighbors()
    {
        // Top left
        turnLeft();
        if(frontIsClear())
        {
            move();
            turnLeft();
            if (frontIsClear())
            {
                move();
                turnAround();
                if (ballsPresent())
                {
                    takeBall();
                    if (ballsPresent())
                    {
                        takeBall();
                        if (ballsPresent())
                        {
                            putBall();
                            putBall();
                            move();
                            down();
                            putBall();
                        }
                        else
                        {
                            putBall();
                            putBall();
                            move();
                            down();
                        }
                    }
                    else
                    {
                        putBall();
                        move();
                        down();
                        putBall();
                    }
                }
                else
                {
                    move();
                    down();
                }
            }
            else
            {
                turnAround();
                down();
            }
        }
        else
        {
            turnRight();
        }
        
        // Top middle
        turnLeft();
        if (frontIsClear())
        {
            move();
            turnRight();
            if (ballsPresent())
            {
                takeBall();
                if (ballsPresent())
                {
                    takeBall();
                    if (ballsPresent())
                    {
                        putBall();
                        putBall();
                        down();
                        putBall();
                    }
                    else
                    {
                        putBall();
                        putBall();
                        down();
                    }
                }
                else
                {
                    putBall();
                    down();
                    putBall();
                }
            }
            else
            {
                down();
            }
        }
        else
        {
            turnRight();
        }
        
        // Top right
        turnLeft();
        if(frontIsClear())
        {
            move();
            turnRight();
            if (frontIsClear())
            {
                move();
                if (ballsPresent())
                {
                    takeBall();
                    if (ballsPresent())
                    {
                        takeBall();
                        if (ballsPresent())
                        {
                            putBall();
                            putBall();
                            back();
                            down();
                            putBall();
                        }
                        else
                        {
                            putBall();
                            putBall();
                            back();
                            down();
                        }
                    }
                    else
                    {
                        putBall();
                        back();
                        down();
                        putBall();
                    }
                }
                else
                {
                    back();
                    down();
                }
            }
            else
            {
                down();
            }
        }
        else
        {
            turnRight();
        }
        
        // Right
        if (frontIsClear())
        {
            move();
            if (ballsPresent())
            {
                takeBall();
                if (ballsPresent())
                {
                    takeBall();
                    if (ballsPresent())
                    {
                        putBall();
                        putBall();
                        back();
                        putBall();
                    }
                    else
                    {
                        putBall();
                        putBall();
                        back();
                    }
                }
                else
                {
                    putBall();
                    back();
                    putBall();
                }
            }
            else {
                back();
            }
        }
        
        // Bottom right
        turnRight();
        if(frontIsClear()) {
            move();
            turnLeft();
            if (frontIsClear()) {
                move();
                
                if (ballsPresent()) {
                    takeBall();
                    if (ballsPresent()) {
                        takeBall();
                        if (ballsPresent()) {
                            putBall();
                            putBall();
                            back();
                            up();
                            putBall();
                        }
                        else {
                            putBall();
                            putBall();
                            back();
                            up();
                        }
                    }
                    else {
                        putBall();
                        back();
                        up();
                        putBall();
                    }
                }
                else {
                    back();
                    up();
                }
            }
            else {
                up();
            }
        }
        else {
            turnLeft();
        }
        
        // Bottom middle
        turnRight();
        if (frontIsClear()) {
            move();
            turnLeft();
            
            if (ballsPresent()) {
                takeBall();
                if (ballsPresent()) {
                    takeBall();
                    if (ballsPresent()) {
                        putBall();
                        putBall();
                        up();
                        putBall();
                    }
                    else {
                        putBall();
                        putBall();
                        up();
                    }
                }
                else {
                    putBall();
                    up();
                    putBall();
                }
            }
            else {
                up();
            }
        }
        else {
            turnLeft();
        }
        
        // Bottom left
        turnRight();
        if(frontIsClear()) {
            move();
            turnRight();
            if (frontIsClear()) {
                move();
                turnAround();
                
                if (ballsPresent()) {
                    takeBall();
                    if (ballsPresent()) {
                        takeBall();
                        if (ballsPresent()) {
                            putBall();
                            putBall();
                            move();
                            up();
                            putBall();
                        }
                        else {
                            putBall();
                            putBall();
                            move();
                            up();
                        }
                    }
                    else {
                        putBall();
                        move();
                        up();
                        putBall();
                    }
                }
                else {
                    move();
                    up();
                }
            }
            else {
                turnAround();
                up();
            }
        }
        else {
            turnLeft();
        }
        
        // Left
        turnAround();
        if (frontIsClear()) {
            move();
            
            if (ballsPresent()) {
                takeBall();
                if (ballsPresent()) {
                    takeBall();
                    if (ballsPresent()) {
                        putBall();
                        putBall();
                        back();
                        putBall();
                    }
                    else {
                        putBall();
                        putBall();
                        back();
                    }
                }
                else {
                    putBall();
                    back();
                    putBall();
                }
            }
            else {
                back();
            }
        }
        turnAround();
    }
    
    private void up() {
        turnLeft();
        move();
        turnRight();
    }
    
    private void down() {
        turnRight();
        move();
        turnLeft();
    }
    
    private void back() {
        turnAround();
        move();
        turnAround();
    }
    
    private void moveToEnd() {
        while (frontIsClear()) {
                move();
            }
    }
    
}