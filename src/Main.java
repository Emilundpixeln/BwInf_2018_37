import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

class Main {

    static final int COUNT = 1000000;
    static final Random rng = new Random();



    public static void main(String[] args)
    {

        int[] list = { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150, 155, 160, 165, 170, 175, 180, 185, 190, 195, 200, 205, 210, 215, 220, 225, 230, 235, 240, 245, 250, 255, 260, 265, 270, 275, 280, 285, 290, 295, 300, 305, 310, 315, 320, 325, 330, 335, 340, 345, 350, 355, 360, 365, 370, 375, 380, 385, 390, 395, 400, 405, 410, 415, 420, 425, 430, 435, 440, 445, 450, 455, 460, 465, 470, 475, 480, 485, 490, 495, 500, 505, 510, 515, 520, 525, 530, 535, 540, 545, 550, 555, 560, 565, 570, 575, 580, 585, 590, 595, 600, 605, 610, 615, 620, 625, 630, 635, 640, 645, 650, 655, 660, 665, 670, 675, 680, 685, 690, 695, 700, 705, 710, 715, 720, 725, 730, 735, 740, 745, 750, 755, 760, 765, 770, 775, 780, 785, 790, 795, 800, 805, 810, 815, 820, 825, 830, 835, 840, 845, 850, 855, 860, 865, 870, 875, 880, 885, 890, 895, 900, 905, 910, 915, 920, 925, 930, 935, 940, 945, 950, 955, 960, 965, 970, 975, 980, 985, 990, 995};
        int c = 10;
        Arrays.sort(list);
        int minEle = list[0];
        int range = list[list.length - 1] + 1 - minEle;
        List<Integer> l = new ArrayList<>(list.length);
        for (int i : list) {
            l.add(i);
        }


        int min = Integer.MAX_VALUE;

        for (int i = 0; i < COUNT; i++) {
            List<Integer> choices = new ArrayList<>(c);
            for (int j = 0; j < c; j++) {
                choices.add(Math.abs(rng.nextInt()) % range + minEle);

            }

            int money = 0;

            for (int player : l) {
                int minMoney = Integer.MAX_VALUE;
                for (int choice : choices) {
                    minMoney = Math.min(minMoney, Math.abs(player - choice));
                }
                money += minMoney;
            }
            if(money < min)
            {
                min = money;
                System.out.println(choices + " " + money);
            }

        }

        int[] res = alCapone(list, c);
        for (int i : res) {
            System.out.println(i);
        }
        int money = 0;
        for (int player : l) {
            int minMoney = Integer.MAX_VALUE;
            for (int choice : res) {
                minMoney = Math.min(minMoney, Math.abs(player - choice));
            }
            money += minMoney;
        }
        System.out.println("Er zahlt " + money + "$ aus.");
    }

    static class Group
    {
        int originIndex;
        int targetDistance;
        int originAfterTarget;
        int beginIndex;
        int endIndex;
        int sum;
        Group next;
        Group(int pos, int val)
        {
            originIndex = pos;
            beginIndex = pos;
            endIndex = pos;
            sum = val;
        }

    }

    public static int[] alCapone(int[] choices, int numbersToPick)
    {
        Arrays.sort(choices);

        Group[] groups = new Group[numbersToPick];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = new Group(i, choices[i]);
        }
        for (int i = 0; i < groups.length; i++) {
            groups[i].next = i == groups.length -1 ? null : groups[i + 1];
        }
        Group last = groups[groups.length - 1];
        while (last.endIndex != choices.length - 1)
        {
            // compute targetDistance
            int lowestIndex = 0;
            int lowestDistance = Integer.MAX_VALUE;
            for (int i = 0; i < groups.length; i++)
            {
                Group g = groups[i];
                int NewSum = g.sum + choices[g.endIndex + 1];
                int avr = NewSum / (g.endIndex - g.beginIndex + 2);
                g.originAfterTarget = nearest(choices, avr, g.originIndex, g.beginIndex, g.endIndex + 1);
                g.targetDistance = choices[g.endIndex + 1] - choices[g.originAfterTarget];
                if(g.targetDistance < lowestDistance)
                {
                    lowestDistance = g.targetDistance;
                    lowestIndex = i;
                }
            }


            do {
                int i = lowestIndex;
                Group g = groups[i];
                if(g == last)
                {
                    g.originIndex = g.originAfterTarget;
                    g.endIndex++;
                    g.sum += choices[g.endIndex];
                    break;
                }

                Group next = g.next;

                if(g.targetDistance <= next.targetDistance)
                {
                    // move
                    if(next.originIndex == next.beginIndex)
                        next.originIndex++;

                    next.sum -= choices[next.beginIndex];
                    next.beginIndex++;

                    if(next.beginIndex - 1 == next.endIndex)
                    {
                        if(next != last)
                        {
                            // move to the end
                            g.next = next.next;
                            last.next = next;
                        }


                        int newIndex = last.endIndex + 1;
                        next.beginIndex = newIndex;
                        next.endIndex = newIndex;
                        next.originIndex = newIndex;
                        next.sum = choices[newIndex];
                        last = next;
                    }

                    int avr = next.sum  / (g.endIndex - g.beginIndex + 2);
                    next.originIndex  = nearest(choices, avr, next.originIndex, next.beginIndex, next.endIndex + 1);

                    g.originIndex = g.originAfterTarget;
                    g.endIndex++;
                    g.sum += choices[g.endIndex];



                    break;
                }
            } while (false);
            /*
            String s = "";
            for (int i = 0; i < choices.length; i++) {
                s += String.format("%4d", choices[i]);
            }
            s += "\n";
            for (int i = 0; i < choices.length; i++)
            {
                for (int j = 0; j < groups.length; j++)
                    if(i >= groups[j].beginIndex && i <= groups[j].endIndex) {
                        s += String.format("%4d", j);
                        break;
                    }
            }
            System.out.println(s);
            for (int i = 0; i < groups.length; i++) {
                int next = -1;
                for (int j = 0; j < groups.length; j++)
                    if(groups[i].next.originIndex == groups[j].originIndex)
                        next = j;
                System.out.println(i + "ta" + groups[i].targetDistance + " or " + choices[groups[i].originIndex] + "->" + next);
            }
            */
        }

        int[] res = new int[groups.length];
        for (int i = 0; i < groups.length; i++) {
            res[i] = choices[groups[i].originIndex];
        }
        return res;
    }

    private static int nearest(int[] choices, int avr, int curIndex, int minIndex, int maxIndex)
    {
        int nearest = choices[curIndex];
        if(avr == nearest)
            return curIndex;

        int index = curIndex;

        if(avr > nearest)
        {
            do {
                nearest = choices[++index];
            } while (avr > nearest && index < maxIndex);
            int low = choices[index - 1];
            int high = choices[index];
            return Math.abs(avr - low) < Math.abs(avr - high) ? index - 1 : index;
        }

        else
        {
            do {
                nearest = choices[--index];
            } while (avr < nearest && index > minIndex);
            int low = choices[index];
            int high = choices[index + 1];
            return Math.abs(avr - low) < Math.abs(avr - high) ? index : index + 1;
        }

    }


}