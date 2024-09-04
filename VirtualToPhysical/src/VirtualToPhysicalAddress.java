//imports
import java.io.*;
/**
 *A code to translate virtual address to physical address
 *
 * Author: Mziwokholo Tshem
 * Date: 20 April 2024
 */

public class VirtualToPhysicalAddress {
        /**GLOBAL VARRIABLES**************************************************************** */
        //CREATE ARRAY TO STORE BYTES
        public static byte[] buffer = new byte[28488]; /**arrays of bytes */
        public static int[] bytesArray = new int[28488]; /**arrays unsigned int values of bytes */
        public static String[] addressArray = new String[28488/8];/** Creante an array to store(8 BIT Binary STRING VALUES OF the adresses in file) hence dividing total bytes */ 
        public static int bytesRead; /**A REFFERENCE FOR BYES IN ARRAY */
        
        // Path to the file
        public static String fileName = "VirtualAddresses";

        //CREATE VARRIABLES FOR PAGE TABLE SIZE AND LOOKUP TABLE
        public static final int pageSize= 128;  /** Size of a page and frame (in bytes) */  
    	public static final int[] lookupTable = {2, 4, 1, 7, 3, 5, 6};/** lookup table */

        /**GLOBAL VARRIABLES***************************************************************** */

    /**MAIN METHOD******************************************************************************* */
    /**@param args no cmd args*/
    public static void main(String[] args) {
        
        try (FileInputStream inputFile = new FileInputStream(fileName)) {
            /**READ THE FILE TILL THE END */
            while ((bytesRead = inputFile.read(buffer)) != -1) {
                // Iterate through the bytes read
                for (int i = 0; i < bytesRead; i++) {
                    // Interpret each byte as an unsigned byte (0-255)
                    int positiveInteger = buffer[i] & 0xFF; /**use this bitmask */
                    bytesArray[i] = positiveInteger; // ADD unsigned integer values BYTES bytesArray[]
                   
                }
                int i = 0;
                int x = 0; /**this is and index of binary string array */
                while (i < bytesArray.length ) {
                //starting from LSB
                int rightByte = bytesArray[i]; /**start form first byte */
        		int leftByte = bytesArray[i+1]; /**and second byt */
        		/**create binary string using the last 2 bits, I NOTICED ALL OTHER BITS ARE THE SAME FOR ALL VALUES (WHICH IS 0) */
        		String binaryString = intToBinaryString(leftByte)+intToBinaryString(rightByte);/**concatenate them in reverse order */
        		addressArray[x] = binaryString;
                
                i += 8; /**jump by 8 butyes */
                x++;
                }
                
            }
            for (int j = 0; j < addressArray.length; j++){
                // open new file to write HEX STRING VALUES of physical addresses
                FileWriter outputFile = new FileWriter("output-OS1",true);
                outputFile.write("0x"+ signExtend(Integer.toHexString(translateAddress(binaryStringToInt(addressArray[j])))).toUpperCase()+"\n");
                outputFile.close();
                }
            // When the file is reached, inform the user
            System.out.println("Execution completed.");
            
            } catch (IOException e) {
                    e.printStackTrace();
        }
    }
    /**MAIN METHOD******************************************************************************* */
    
    //METHOD 1: covert given integer byte in to binary string
    
    /**
     * @param integerNumber * @return 8-BIT Binary String OF THE BYTE INTERGER VALUE */
    public static String intToBinaryString(int integerNumber) {
        integerNumber = integerNumber & 0b11111111; //bismask used
        String binaryString = Integer.toBinaryString(integerNumber);// Convert the integer to a binary string using Integer.toBinaryString
        
        return signExtend(binaryString);/**Perform sign extention to 8 bits*/  
    }
    
     //METHOD 2: covert given binary string in to binary string an integer using Integer.parseInt to use for calculations
    
     /**
     * @param binaryString * @return integerValue equivalent */
     public static int binaryStringToInt(String binaryString) {
        // Parse the binary string to an integer using Integer.parseInt
        int intValue = Integer.parseInt(binaryString, 2);
        
        return intValue;
    }
    //METHOD 3: ADDRESS TRANSLATION
    
    /**
     * @param virtualAddress * @return PhysialAddress */
    public static int translateAddress(int virtualAddress) {
       
        int offset = virtualAddress & (pageSize - 1); // Extract page offset //bitmask of 127
        int virtualPageNumber = virtualAddress >>7; // Extract the virtualPageNumber by shifting right 7 bits log2(PAGE_SIZE)= 7;
        int physicalFrameNumber = lookupTable[virtualPageNumber]; // once you get virtualPageNumber use lookup table to find the physical page matching with it
        
        return (physicalFrameNumber << 7) | offset; // Combine physicalFrameNumber and offset to get the physical address, sll 7 address size
    }
    //METHOD 4 SIGN EXTENTION
 
    /**
     * @param value * @return 8-bit String on the value */
    public static String signExtend(String value){
          String bytesString = value;
          for (int i = 0; i < 8 - value.length(); i++) {
            bytesString ="0"+bytesString;
            }

        return bytesString;
    } 
}

