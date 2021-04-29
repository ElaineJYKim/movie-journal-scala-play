package javac.bertModel;

import org.tensorflow.DataType;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.ndarray.FloatNdArray;
import org.tensorflow.ndarray.IntNdArray;
import org.tensorflow.ndarray.NdArrays;
import org.tensorflow.ndarray.Shape;
import org.tensorflow.ndarray.buffer.ByteDataBuffer;
import org.tensorflow.ndarray.buffer.DataBuffers;
import org.tensorflow.ndarray.buffer.FloatDataBuffer;
import org.tensorflow.ndarray.buffer.IntDataBuffer;
import org.tensorflow.proto.framework.SignatureDef;
import org.tensorflow.types.TFloat32;
import org.tensorflow.types.TInt32;
import org.tensorflow.types.family.TType;

import javax.inject.Singleton;
import java.util.*;

@Singleton
public final class BertModel {
    private final SavedModelBundle bertModel;
    private final javac.javaTokenization.FullTokenizer tokenizer;
    private final Integer MAXLEN = 100;

    public BertModel(javac.javaTokenization.FullTokenizer t) {
        bertModel = loadModel();
        tokenizer = t;
    }

    public SavedModelBundle loadModel() {
        String modelDir = "public/model";
        SavedModelBundle.Loader loader = SavedModelBundle.loader(modelDir).withTags("serve");
        return loader.load();
    }

    public List<Integer> tokenize(String text) {
        List<String> word_tokens = tokenizer.tokenize(text);
        word_tokens.add("[SEP]");
        word_tokens.add(0,"[CLS]");

        List<Integer> token_ids = tokenizer.convertTokensToIds(word_tokens);
        Integer buffer = MAXLEN - token_ids.size();
//        List<Integer> bufferList = new ArrayList<Integer>(Collections.nCopies(MAXLEN - token_ids.size(), 0));
//        token_ids.addAll(bufferList);

        for (int i = 0; i < buffer; i++){
            token_ids.add(0);
        }

        return token_ids;
    }

    public Integer predict(String text) {
//         https://stackoverflow.com/questions/62241546/java-tensorflow-keras-equivalent-of-model-predict
//         https://github.com/tensorflow/java/issues/134
//         https://github.com/tensorflow/java/tree/master/ndarray
//         https://stackoverflow.com/questions/60783153/tensorflow-in-scala-reflection
//         https://github.com/tensorflow/java/pull/92

        // Session session = bertModel.session();
        // SignatureDef modelInfo = bertModel.metaGraphDef().getSignatureDefMap().get("serving_default");

        List<Integer> token_ids = tokenize(text);

        // Convert to tensor
        IntDataBuffer bufferTokens = DataBuffers.ofInts(100);
        int[] primArr = new int[100];
        for (int i=0; i<MAXLEN; i++) {
            primArr[i] = token_ids.get(i);
        }
        bufferTokens.write(primArr);

        IntNdArray tokensMatrix = NdArrays.ofInts(Shape.of(1, 100));
        IntNdArray vector = tokensMatrix.get(0);
        vector.write(bufferTokens);

        Tensor<TInt32> input = TInt32.tensorOf(tokensMatrix);

        // Model.predict
        Tensor output = bertModel.session()
                .runner()
                .feed("serving_default_input_ids:0", input)
                .fetch("StatefulPartitionedCall:0")
                .run()
                .get(0);

        // Extract arg max from tensor
        Tensor val = output.expect(TFloat32.DTYPE); // FLOAT (1) tensor with shape [1, 3]
        FloatDataBuffer floats = output.rawData().asFloats(); // org.tensorflow.ndarray.impl.buffer.raw.FloatRawDataBuffer@d1ac1bff

        // Float f1 = floats.getFloat(0);
        // Float f2 = floats.getFloat(1);
        // Float f3 = floats.getFloat(2);
        // Arrays.asList(f1,f2,f3);

        int maxAt = 0;

        for (int i = 0; i < 3; i++) {
            maxAt = floats.getFloat(i) > floats.getFloat(maxAt) ? i : maxAt;
        }

        return maxAt;
    }
}


// ModelInfo
//    inputs {
//        key: "input_ids"
//        value {
//            name: "serving_default_input_ids:0"
//            dtype: DT_INT32
//            tensor_shape {
//                dim {
//                    size: -1
//                }
//                dim {
//                    size: 100
//                }
//            }
//        }
//    }
//    outputs {
//        key: "dense_3"
//        value {
//            name: "StatefulPartitionedCall:0"
//            dtype: DT_FLOAT
//            tensor_shape {
//                dim {
//                    size: -1
//                }
//                dim {
//                    size: 3
//                }
//            }
//        }
//    }
//method_name: "tensorflow/serving/predict"