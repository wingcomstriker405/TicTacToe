import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TicTacToe extends JPanel implements MouseListener
{
    public static void main(String[] args)
    {
        new TicTacToe();
    }

    private int[][] field;
    private boolean secondPlayerMove;
    private String winnerText;

    private final int ww = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private final int wh = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    private final int fieldBuffer = 50;
    private final int fieldSize = (Math.min(ww, wh) - fieldBuffer * 2) / 3;
    private final int fieldSymbolBuffer = fieldSize / 10;
    private final int fieldOffsetX = (ww - fieldSize * 3) / 2;
    private final int fieldOffsetY = (wh - fieldSize * 3) / 2;

    private final JFrame frame;

    private final Font winnerTextFont = new Font(Font.DIALOG, Font.BOLD, 120);

    private final Color backgroundColor = new Color(25, 25, 25);
    private final Color lineColor = new Color(200, 200, 200);
    private final Color drawTextColor = new Color(255, 255, 255);
    private final Color player1TextColor = new Color(255, 0, 0);
    private final Color player2TextColor = new Color(0, 0, 255);
    private final Color player1Color = new Color(200, 0, 0);
    private final Color player2Color = new Color(0, 0, 200);

    private TicTacToe()
    {
        this.field = new int[3][3];
        addMouseListener(this);
        setBounds(0, 0, this.ww, this.wh);
        setPreferredSize(new Dimension(this.ww, this.wh));
        setVisible(true);

        this.frame = new JFrame();
        this.frame.add(this);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setUndecorated(true);
        this.frame.setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics a)
    {
        Graphics2D g = (Graphics2D) a;
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(this.backgroundColor);
        g.fillRect(0, 0, this.ww, this.wh);

        g.setStroke(new BasicStroke(8));
        g.setColor(this.lineColor);
        for(int i = 0; i <= 3; i++)
        {
            g.drawLine(this.fieldOffsetX + i * this.fieldSize, this.fieldOffsetY, this.fieldOffsetX + i * this.fieldSize, this.fieldOffsetY + this.fieldSize * 3);
            g.drawLine(this.fieldOffsetX, this.fieldOffsetY + i * this.fieldSize, this.fieldOffsetX + this.fieldSize * 3, this.fieldOffsetY + i * this.fieldSize);
        }

        g.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        for(int i = 0; i < this.field.length; i++)
        {
            for(int k = 0; k < this.field[i].length; k++)
            {
                if(this.field[i][k] == 1) drawCross(g, i, k);
                else if(this.field[i][k] == 2) drawOval(g, i, k);
            }
        }

        if(this.winnerText != null)
        {
            g.setFont(this.winnerTextFont);
            g.setColor(this.winnerText.equals("Player 1 has won!")? this.player1TextColor : this.winnerText.equals("Player 2 has won!")? this.player2TextColor : this.drawTextColor);
            int width = g.getFontMetrics().stringWidth(this.winnerText);
            int height = g.getFontMetrics().getAscent();
            g.drawString(this.winnerText, (this.ww - width) / 2, (this.wh - height) / 2);
        }
    }

    private void drawCross(Graphics2D g, int x, int y)
    {
        g.setColor(this.player1Color);
        g.drawLine(this.fieldOffsetX + x * this.fieldSize + this.fieldSymbolBuffer, this.fieldOffsetY + y * this.fieldSize + this.fieldSymbolBuffer, this.fieldOffsetX + (x + 1) * this.fieldSize - this.fieldSymbolBuffer, this.fieldOffsetY + (y + 1) * this.fieldSize - this.fieldSymbolBuffer);
        g.drawLine(this.fieldOffsetX + (x + 1) * this.fieldSize - this.fieldSymbolBuffer, this.fieldOffsetY + y * this.fieldSize + this.fieldSymbolBuffer, this.fieldOffsetX + x * this.fieldSize + this.fieldSymbolBuffer, this.fieldOffsetY + (y + 1) * this.fieldSize - this.fieldSymbolBuffer);
    }

    private void drawOval(Graphics2D g, int x, int y)
    {
        g.setColor(this.player2Color);
        g.drawOval(this.fieldOffsetX + x * this.fieldSize + this.fieldSymbolBuffer, this.fieldOffsetY + y * this.fieldSize + this.fieldSymbolBuffer, this.fieldSize - this.fieldSymbolBuffer * 2, this.fieldSize - this.fieldSymbolBuffer * 2);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if(this.winnerText == null)
        {
            int px = (e.getX() - this.fieldOffsetX);
            int py = (e.getY() - this.fieldOffsetY);
            if(0 <= px && 0 <= py)
            {
                int x = px / this.fieldSize;
                int y = py / this.fieldSize;
                if(0 <= x && x < 3 && 0 <= y && y < 3)
                {
                    if(this.field[x][y] == 0)
                    {
                        this.field[x][y] = (this.secondPlayerMove = !this.secondPlayerMove)? 1 : 2;
                        this.winnerText = checkForWinner();
                    }
                }
            }
        }
        else
        {
            this.secondPlayerMove = this.winnerText.equals("Player 1 has won!");
            this.winnerText = null;
            this.field = new int[3][3];
        }
        repaint();
    }

    private String checkForWinner()
    {
        for(int i = 0; i < 3; i++)
            if(this.field[i][0] == this.field[i][1] && this.field[i][1] == this.field[i][2] && this.field[i][0] != 0)
                return "Player " + this.field[i][0] + " has won!";

        for(int i = 0; i < 3; i++)
            if(this.field[0][i] == this.field[1][i] && this.field[1][i] == this.field[2][i] && this.field[0][i] != 0)
                return "Player " + this.field[0][i] + " has won!";

        if(this.field[0][0] == this.field[1][1] && this.field[1][1] == this.field[2][2] && this.field[0][0] != 0)
            return "Player " + this.field[0][0] + " has won!";

        if(this.field[0][2] == this.field[1][1] && this.field[1][1] == this.field[2][0] && this.field[0][2] != 0)
            return "Player " + this.field[0][2] + " has won!";

        for(int i = 0; i < 3; i++)
            for(int k = 0; k < 3; k++)
                if(this.field[i][k] == 0)
                    return null;

        return "Draw!";
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mouseReleased(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {

    }

    @Override
    public void mouseExited(MouseEvent e)
    {

    }
}
