import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class OutputPrinter implements Printable
{
    private final String printData;

    public OutputPrinter(String printDataIn)
    {
        this.printData = printDataIn;
    }

    @Override
    public int print(Graphics g, PageFormat pf, int page) {
        if (page > 0)
        {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D)g;
        int x = (int) pf.getImageableX();
        int y = (int) pf.getImageableY();
        g2d.translate(x, y);

        Font font = new Font("Serif", Font.PLAIN, 10);
        FontMetrics metrics = g.getFontMetrics(font);
        int lineHeight = metrics.getHeight();

        BufferedReader br = new BufferedReader(new StringReader(printData));

        try
        {
            String line;
            x += 50;
            y += 50;
            while ((line = br.readLine()) != null)
            {
                y += lineHeight;
                g2d.drawString(line, x, y);
            }
        }
        catch (IOException ignored)
        {
        }

        return PAGE_EXISTS;
    }
}