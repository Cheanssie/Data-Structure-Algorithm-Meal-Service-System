/*
*Title : Validators
*Purpose : Collection of Validation Methods
 */
package Validation;

public class Validator {

    public static int countWord(String sentence) {
        int spaceNum = 0;
        int index = 0;

        do {
            index = sentence.indexOf(" ", index);
            if (index != -1) {
                spaceNum++;
                index++;
            }
        } while (index != -1);

        return spaceNum;
    }
}
