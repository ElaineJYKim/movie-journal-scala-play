
/* Copyright 2019 The TensorFlow Authors. All Rights Reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/
package javac.javaTokenization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.io.File;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Singleton;

/**
 * A java realization of Bert tokenization. Original python code:
 * https://github.com/google-research/bert/blob/master/tokenization.py runs full tokenization to
 * tokenize a String into split subtokens or ids.
 */

@Singleton
public final class FullTokenizer {
    private final javac.javaTokenization.BasicTokenizer basicTokenizer;
    private final javac.javaTokenization.WordpieceTokenizer wordpieceTokenizer;
    private final Map<String, Integer> dic;

    // Map<String, Integer> inputDic
    public FullTokenizer() {
        dic = inputDic();
        basicTokenizer = new javac.javaTokenization.BasicTokenizer(true);
        wordpieceTokenizer = new javac.javaTokenization.WordpieceTokenizer(dic);
    }

    public Map<String, Integer> inputDic() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Integer> inputDic = mapper.readValue(
                    new File("public/files/vocabMap.json"),
                    new TypeReference<Map<String, Integer>>(){}
                    );
            return inputDic;

        } catch (Exception e) {

            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public List<String> tokenize(String text) {
        List<String> splitTokens = new ArrayList<>();
        for (String token : basicTokenizer.tokenize(text)) {
            splitTokens.addAll(wordpieceTokenizer.tokenize(token));
        }
        return splitTokens;
    }

    public List<Integer> convertTokensToIds(List<String> tokens) {
        List<Integer> outputIds = new ArrayList<>();
        for (String token : tokens) {
            outputIds.add(dic.get(token));
        }
        return outputIds;
    }
}