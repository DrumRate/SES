package ses;

import java.util.ArrayList;

public class BayesModule {
    private ArrayList<HypotezItem> hypotezP;
    private int currentAnswer = 0;
    private DataJSON dataJSON;
    private double min;
    private double max;
    private double avg;

    public BayesModule() {
        hypotezP = new ArrayList<>();
    }

    public void initHypithes() {
        for (Hypothesi item : dataJSON.hypothesis) {
            hypotezP.add(new HypotezItem(item.name, item.statrWith));
        }
    }

    public void nexStep(double answer) {
        if (answer == avg) {
            currentAnswer++;
            return;
        }
        for (int i = 0; i < hypotezP.size(); i++) {
            HypotezItem item = hypotezP.get(i);
            Hypothesis hypothesis = dataJSON.hypothesis.get(i).hypothesiss.get(currentAnswer);
            double pPlus = hypothesis.ifTrue;
            double pMinus = hypothesis.ifFalse;
            if (answer < avg) {
                //net
                double p_h_e = ((1 - pPlus) * item.pi) / ((1 - pPlus) * item.pi + (1 - pMinus) * (1 - item.pi));
                item.pi = p_h_e + ((answer - min) * (item.pi - p_h_e)) / (avg - min);
            } else {
                //da
                double p_h_e = (pPlus * item.pi) / (pPlus * item.pi + pMinus * (1 - item.pi));
                item.pi = item.pi + ((answer - avg) * (p_h_e - item.pi)) / (max - avg);
            }
        }
        currentAnswer++;
    }

    public DataJSON getDataJSON() {
        return dataJSON;
    }

    public void setDataJSON(DataJSON dataJSON) {
        this.dataJSON = dataJSON;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public void calcAvg() {
        avg = (min + max) / 2;
    }

    public ArrayList<HypotezItem> getHypotezP() {
        return hypotezP;
    }
}
