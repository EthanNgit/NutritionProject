package com.example.nutritionproject.Custom.java.NutritionLabelScanner;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.example.nutritionproject.AddFoodItemActivity;
import com.example.nutritionproject.Custom.java.Enums.Nutrient;
import com.example.nutritionproject.Custom.java.Utility.Event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NutritionLabelParser {
    /*
    By using a switch statement and multiple sets I would say it is much less memory and dev friendly performant
    But it is more speed performant (by extension there for user friendly), as i can pick and choose which set to use
    to save iterating over for sure not used elements each time.

     */

    public Event onNutritionalInformationUpdated = new Event();

    private Context context;
    private Queue parsingQueue = new PriorityQueue<String>();
    private HashMap<Nutrient, Pair<Double, NutrientMeasurement>> parsedNutrientsMap = new HashMap<>();
    private HashMap<Nutrient, Set<String>> macroSynonymsMap = new HashMap<>();

    public NutritionLabelParser(Context context, boolean shouldParseVitamins) {
        this.context = context;

        //region Setting Parsing keywords (this is likely to be slow, but it is a one time startup cost)
        try {
            InputStream filePath = context.getAssets().open("ParsingWords.txt");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(filePath));

            int line = 0;
            String synonyms = "";
            while ((synonyms = bufferedReader.readLine()) != null) {
                line++;

                if (!shouldParseVitamins && line > 22) {
                    break;
                }

                if (line % 2 == 0) {
                    // Can check for synonyms
                    switch (line) {
                        case 2:
                            macroSynonymsMap.put(Nutrient.Calorie, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 4:
                            macroSynonymsMap.put(Nutrient.Protein, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 6:
                            macroSynonymsMap.put(Nutrient.TotalCarb, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 8:
                            macroSynonymsMap.put(Nutrient.DietaryFiber, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 10:
                            macroSynonymsMap.put(Nutrient.TotalSugar, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 12:
                            macroSynonymsMap.put(Nutrient.AddedSugar, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 14:
                            macroSynonymsMap.put(Nutrient.TotalFat, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 16:
                            macroSynonymsMap.put(Nutrient.SaturatedFat, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 18:
                            macroSynonymsMap.put(Nutrient.TransFat, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 20:
                            macroSynonymsMap.put(Nutrient.Cholesterol, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 22:
                            macroSynonymsMap.put(Nutrient.Sodium, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 24:
                            macroSynonymsMap.put(Nutrient.Biotin, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 26:
                            macroSynonymsMap.put(Nutrient.Choline, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 28:
                            macroSynonymsMap.put(Nutrient.Folate, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 30:
                            macroSynonymsMap.put(Nutrient.Niacin, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 32:
                            macroSynonymsMap.put(Nutrient.A, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 34:
                            macroSynonymsMap.put(Nutrient.B5, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 36:
                            macroSynonymsMap.put(Nutrient.B6, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 38:
                            macroSynonymsMap.put(Nutrient.B12, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 40:
                            macroSynonymsMap.put(Nutrient.C, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 42:
                            macroSynonymsMap.put(Nutrient.D, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 44:
                            macroSynonymsMap.put(Nutrient.E, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 46:
                            macroSynonymsMap.put(Nutrient.K, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 48:
                            macroSynonymsMap.put(Nutrient.Thiamine, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 50:
                            macroSynonymsMap.put(Nutrient.Riboflavin, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 52:
                            macroSynonymsMap.put(Nutrient.Calcium, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 54:
                            macroSynonymsMap.put(Nutrient.Chloride, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 56:
                            macroSynonymsMap.put(Nutrient.Chromium, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 58:
                            macroSynonymsMap.put(Nutrient.Copper, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 60:
                            macroSynonymsMap.put(Nutrient.Iodine, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 62:
                            macroSynonymsMap.put(Nutrient.Iron, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 64:
                            macroSynonymsMap.put(Nutrient.Magnesium, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 66:
                            macroSynonymsMap.put(Nutrient.Maganese, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 68:
                            macroSynonymsMap.put(Nutrient.Molybdenum, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 70:
                            macroSynonymsMap.put(Nutrient.Phosphorus, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 72:
                            macroSynonymsMap.put(Nutrient.Potassium, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 74:
                            macroSynonymsMap.put(Nutrient.Selenium, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                        case 76:
                            macroSynonymsMap.put(Nutrient.Zinc, new HashSet<>(Arrays.asList(synonyms.split(","))));
                            break;
                    }

                    System.out.println(line);
                }

            }
        } catch (IOException ex) {
            // Should not happen
        }
        //endregion
    }


    public void enqueueParse(String line) {
        parsingQueue.add(line);
    }

    public void parse() {
        while (!parsingQueue.isEmpty()) {
            nutrientParse((String)parsingQueue.poll());
        }
    }

    public HashMap<Nutrient, Pair<Double, NutrientMeasurement>> getParsedNutrients() {
        return parsedNutrientsMap;
    }

    public void resetParse() {
        parsedNutrientsMap.clear();
        onNutritionalInformationUpdated.invoke();
    }

    public void finishParse(@Nullable UnitCallback callback) {
        try {
            AddFoodItemActivity.receivedMacros = getParsedNutrients();
            callback.onSuccess();
        } catch (NullPointerException e) {

        }
    }

    private void nutrientParse(String line) {
        line = line.toLowerCase().trim();

        // Things to ignore for all cases
        if (line == null || line.isEmpty() || line.contains("%")) {
            return;
        }

        // Nutrient by g, mg, mcg (no calories or texts that have values before name)
        if (!parseNormalNutrients(line)) {
            // Nutrient by kcal or different styles
            parseUniqueNutrients(line);
        }

    }

    private boolean parseNormalNutrients(String line) {
        //This messy setup can be fixed with a better regex expression, but my trials never worked...
        String regexLeftToRight = "([\\D\\s]+)\\s*(\\d+\\.?\\d*/?|o|O)(g|mg|mcg)";
        String regexRightToLeft = "([\\D\\s]+)\\s*(\\d+\\.?\\d*/?|o|O)(g|mg|mcg)\\s*([\\D\\s]+)";
        Pattern rLTR = Pattern.compile(regexLeftToRight);
        Pattern rRTL = Pattern.compile(regexRightToLeft);
        Matcher mLTR = rLTR.matcher(line);
        Matcher mRTL = rRTL.matcher(line);

        if (!checkRTLLine(mRTL)) return checkLTRLine(mLTR);

        return true;
    }

    private boolean checkRTLLine(Matcher mRTL) {

        while (mRTL.find()) {
            try {
                if (mRTL.group(4) != null) {
                    String potentialNutrient = mRTL.group(4).trim();
                    String potentialNutrientValue = mRTL.group(2).trim();
                    String potentialNutrientMeasurement = mRTL.group(3).trim();

                    return scanLine(potentialNutrient, potentialNutrientValue, potentialNutrientMeasurement);
                }
            } catch (NullPointerException e) {
                // False nutrient
            }

        }
        return false;
    }

    private boolean checkLTRLine(Matcher mLTR) {
        while (mLTR.find()) {
            try {
                String potentialNutrient = mLTR.group(1).trim();
                String potentialNutrientValue = mLTR.group(2).trim();
                String potentialNutrientMeasurement = mLTR.group(3).trim();

                return scanLine(potentialNutrient, potentialNutrientValue, potentialNutrientMeasurement);

            } catch (NullPointerException e) {
                // False nutrient
            }
        }
        return false;
    }

    private boolean scanLine(String potentialNutrient, String potentialNutrientValue, String potentialNutrientMeasurement) {
        try {
            for (Map.Entry<Nutrient, Set<String>> entry : macroSynonymsMap.entrySet()) {


                // Fix if it parsed a o instead of a 0
                if (potentialNutrientValue.equals("o")) potentialNutrientValue = "0";

                // Try to remove "less than" or '>', where they appear on nutrition labels common nutrients (protein and carbs)
                if (entry.getKey() == Nutrient.Protein || entry.getKey() == Nutrient.TotalCarb || entry.getKey() == Nutrient.TotalSugar) {
                    String tmpPotentialNutrient = potentialNutrient;

                    potentialNutrient = potentialNutrient.replace("less than", "").trim();
                    potentialNutrient = potentialNutrient.replace(">", "").trim();

                    if (!tmpPotentialNutrient.equals(potentialNutrient)) {
                        // Default values less than one to zero
                        potentialNutrientValue = "0";
                    }
                }

                if (entry.getValue().contains(potentialNutrient) && parsedNutrientsMap.get(entry.getKey()) == null) {
                    // Check if added fat and added carbs make sense (otherwise you should push them back out to restart those value)
                    //...
                    parsedNutrientsMap.put(entry.getKey(), new Pair<>(Double.valueOf(potentialNutrientValue), Enum.valueOf(NutrientMeasurement.class, potentialNutrientMeasurement)));
                    Log.d("NORTH_NUTRIENT", "Added: " + entry.getKey().name() + " " + parsedNutrientsMap.get(entry.getKey()).first + parsedNutrientsMap.get(entry.getKey()).second);
                    onNutritionalInformationUpdated.invoke();
                    return true;
                } else {
                    // Fail
                }
            }

        } catch (NullPointerException e) {
            // False nutrient
        }
        return false;
    }

    private boolean parseUniqueNutrients(String line) {
        try {
            if (line.contains("serving size")) {
                // Might be added eventually, but for now, i think it would be easier to hand enter it

            } else {
                // This kinda defeats the purpose of parsing words, but for now...
                String calorieRegex = "([calories\\s]+)\\s*(\\d+)";
                Pattern r = Pattern.compile(calorieRegex);
                Matcher m = r.matcher(line);

                while (m.find()) {
                    if (macroSynonymsMap.get(Nutrient.Calorie).contains(m.group(1).trim()) && parsedNutrientsMap.get(Nutrient.Calorie) == null) {
                        parsedNutrientsMap.put(Nutrient.Calorie, new Pair<>(Double.valueOf(m.group(2).trim()), NutrientMeasurement.none));
                        Log.d("NORTH_NUTRIENT", "Added: " + Nutrient.Calorie.name() + " " + parsedNutrientsMap.get(Nutrient.Calorie).first);
                        onNutritionalInformationUpdated.invoke();
                        return true;
                    }
                }
            }
        } catch (NullPointerException | NumberFormatException e) {
            // Was not calories
        }

        return false;
    }



}
