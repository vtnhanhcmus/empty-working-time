package com.working.time;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

        File file = new File("data_java.xml");
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(file);

        if (doc.hasChildNodes()){

            Demo demo = new Demo();
            LocalDateTime[][] timeMatrix = demo.getTimeMatrix(doc);

            if(timeMatrix.length == 0){
                System.out.println("time is empty");
                return;
            }

            List<TimeEmployee> timeEmployees = demo.timeNotWork(timeMatrix);
            demo.print(timeEmployees);

        }

    }

    /*
     * Make matrix date start and end time
     * @param doc
     * @return
     */
    private LocalDateTime[][] getTimeMatrix(Document doc){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);
        NodeList items = doc.getElementsByTagName(Constants.ELEMENT_TAB_BAR);

        if (items.getLength() == 0){
            return new LocalDateTime[][]{};
        }

        LocalDateTime[][] timeMatrix = new LocalDateTime[items.getLength()][2];
        int k = 0;

        for (int i=0; i< items.getLength(); i++){
            Node node = items.item(i);
            try {
                timeMatrix[k][0] = LocalDateTime.parse(node.getAttributes().getNamedItem(Constants.START_DATE_COLUMN).getNodeValue(), formatter);
                timeMatrix[k][1] = LocalDateTime.parse(node.getAttributes().getNamedItem(Constants.END_DATE_COLUMN).getNodeValue(), formatter);
                k++;
            }catch (Exception ex){
                System.out.println(ex.getLocalizedMessage());
            }

        }

        if (timeMatrix.length <= 0)
        {
            return new LocalDateTime[][]{};
        }

        return timeMatrix;

    }

    /*
     * Fill all time not working employee
     * @param timeMatrix
     * @return
     */
    private List<TimeEmployee> timeNotWork(LocalDateTime[][] timeMatrix){

        // To store the set of free interval
        List<TimeEmployee> timeEmployees = new ArrayList<>();
        // o(nlog(n))
        Arrays.sort(timeMatrix, (a, b) -> a[0].compareTo(b[0]));

        LocalDateTime maxEndDate = timeMatrix[0][1];
        for (int i = 1; i < timeMatrix.length; i++)
        {

            LocalDateTime prevEnd = timeMatrix[i - 1][1];
            LocalDateTime prevStart = timeMatrix[i - 1][0];
            LocalDateTime currStart = timeMatrix[i][0];
            LocalDateTime currEnd = timeMatrix[i][1];

            if (currEnd.isBefore(maxEndDate)){
                maxEndDate = currEnd;
            }

            if (i == 1 && prevStart.toLocalTime().isAfter(Constants.localTimeStart)){
                timeEmployees.add(new TimeEmployee(LocalDateTime.of(prevStart.getYear(),prevStart.getMonth(),prevStart.getDayOfMonth(),9,0,0),
                        currStart));
            }

            if (prevEnd.isBefore(currStart) &&
                    (prevEnd.toLocalTime().isAfter(Constants.localTimeStart) && prevEnd.toLocalTime().isBefore(Constants.localTimeEnd))
                    && (currStart.toLocalTime().isAfter(Constants.localTimeStart) && currStart.toLocalTime().isBefore(Constants.localTimeEnd))) {
                timeEmployees.add(new TimeEmployee(prevEnd,
                        currStart));
            }
        }

        if (maxEndDate.isBefore(LocalDateTime.of(maxEndDate.getYear(),maxEndDate.getMonth(),maxEndDate.getDayOfMonth(),18,0,0))){
            timeEmployees.add(new TimeEmployee(
                    maxEndDate, LocalDateTime.of(maxEndDate.getYear(),maxEndDate.getMonth(),maxEndDate.getDayOfMonth(),18,0,0))
            );
        }


        return timeEmployees;
    }


    /*
     * Print result
     * @param timeEmployees
     */
    private void print(List<TimeEmployee> timeEmployees){

        if (timeEmployees == null || timeEmployees.isEmpty()){
            System.out.println("Not have empty intervals time");
        }

        for (int i = 0; i < timeEmployees.size(); i++)
        {
            System.out.printf("[%s,%s]", timeEmployees.get(i).getStart().toString(), timeEmployees.get(i).getEnd().toString());
            System.out.println();
        }

    }

}
