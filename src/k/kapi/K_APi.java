package k.kapi;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;


class K_Layer {


    static class Dense {


        K_Tensor w;

        String act_fn;


        Dense(int incoming_size, int outgoing_size, String act_fn) {

            this.w = K_Tensor.randn(incoming_size, outgoing_size);
            this.w.requires_grad = true;
            this.act_fn = act_fn;

        }

        Dense(Dense layer) {

            this.w = new K_Tensor(layer.w.matrix);
            this.w.requires_grad = true;
            this.act_fn = layer.act_fn;

        }


    }

    static K_Tensor propogate(Dense layer, K_Tensor incoming) {

        K_Tensor out = K_Tensor.matmul(incoming, layer.w);

        switch (layer.act_fn) {

            case "sigm":
                return K_Tensor.sigm(out);

            case "tanh":
                return K_Tensor.tanh(out);

            //case "relu": // todo : do someday
            //return K_Tensor.relu(out)

        }

        return null;

    }

    static void clear_grad(Dense layer) {

        layer.w.grad = K_Math.zeros(K_Math.size(layer.w.grad));

    }


    static class LSTM {


        K_Tensor wf1, wf2;
        K_Tensor wk1, wk2;
        K_Tensor wi1, wi2;
        K_Tensor ws1, ws2;

        K_Tensor state;


        LSTM(int incoming_size, int outgoing_size) {

            this.wf1 = K_Tensor.randn(incoming_size, outgoing_size); this.wf1.requires_grad = true;
            this.wf2 = K_Tensor.randn(outgoing_size, outgoing_size); this.wf2.requires_grad = true;

            this.wk1 = K_Tensor.randn(incoming_size, outgoing_size); this.wk1.requires_grad = true;
            this.wk2 = K_Tensor.randn(outgoing_size, outgoing_size); this.wk2.requires_grad = true;

            this.wi1 = K_Tensor.randn(incoming_size, outgoing_size); this.wi1.requires_grad = true;
            this.wi2 = K_Tensor.randn(outgoing_size, outgoing_size); this.wi2.requires_grad = true;

            this.ws1 = K_Tensor.randn(incoming_size, outgoing_size); this.ws1.requires_grad = true;
            this.ws2 = K_Tensor.randn(outgoing_size, outgoing_size); this.ws2.requires_grad = true;

            this.state = K_Tensor.zeros(1, outgoing_size);

        }

        LSTM(LSTM layer) {

            this.wf1 = new K_Tensor(layer.wf1.matrix); this.wf1.requires_grad = true;
            this.wf2 = new K_Tensor(layer.wf2.matrix); this.wf2.requires_grad = true;

            this.wk1 = new K_Tensor(layer.wk1.matrix); this.wk1.requires_grad = true;
            this.wk2 = new K_Tensor(layer.wk2.matrix); this.wk2.requires_grad = true;

            this.wi1 = new K_Tensor(layer.wi1.matrix); this.wi1.requires_grad = true;
            this.wi2 = new K_Tensor(layer.wi2.matrix); this.wi2.requires_grad = true;

            this.ws1 = new K_Tensor(layer.ws1.matrix); this.ws1.requires_grad = true;
            this.ws2 = new K_Tensor(layer.ws2.matrix); this.ws2.requires_grad = true;

            this.state = K_Tensor.zeros(K_Tensor.size(layer.state));

        }


    }

    static K_Tensor propogate(LSTM layer, K_Tensor incoming) {

        K_Tensor forget = K_Tensor.sigm(K_Tensor.add(K_Tensor.matmul(incoming, layer.wf1),K_Tensor.matmul(layer.state , layer.wf2)));
        K_Tensor keep   = K_Tensor.sigm(K_Tensor.add(K_Tensor.matmul(incoming, layer.wk1),K_Tensor.matmul(layer.state , layer.wk2)));
        K_Tensor interm = K_Tensor.sigm(K_Tensor.add(K_Tensor.matmul(incoming, layer.wi1),K_Tensor.matmul(layer.state , layer.wi2)));
        K_Tensor show   = K_Tensor.sigm(K_Tensor.add(K_Tensor.matmul(incoming, layer.ws1),K_Tensor.matmul(layer.state , layer.ws2)));

        layer.state = K_Tensor.add(K_Tensor.mul(forget, layer.state), K_Tensor.mul(keep, interm));

        return K_Tensor.mul(show, K_Tensor.tanh(layer.state));

    }

    static void clear_grad(LSTM layer) {

        layer.wf1.grad = K_Math.zeros(K_Math.size(layer.wf1.grad));
        layer.wf2.grad = K_Math.zeros(K_Math.size(layer.wf2.grad));

        layer.wk1.grad = K_Math.zeros(K_Math.size(layer.wk1.grad));
        layer.wk2.grad = K_Math.zeros(K_Math.size(layer.wk2.grad));

        layer.wi1.grad = K_Math.zeros(K_Math.size(layer.wi1.grad));
        layer.wi2.grad = K_Math.zeros(K_Math.size(layer.wi2.grad));

        layer.ws1.grad = K_Math.zeros(K_Math.size(layer.ws1.grad));
        layer.ws2.grad = K_Math.zeros(K_Math.size(layer.ws2.grad));

    }

    static void clear_state(LSTM layer) {

        layer.state = K_Tensor.zeros(1, K_Tensor.size(layer.wf2,1));

    }


}


class K_Model {


    // FeedForward

//    static List<K_Layer.Dense> FeedForward(int in_size, int[] hidden_sizes, int out_size, String activation_fn) {
//
//        List<K_Layer.Dense> model = new ArrayList<>();
//
//        int ctr = -1;
//        for (int hidden_size : hidden_sizes) {
//            ctr++;
//
//            if (ctr == 0)
//
//                model.add(new K_Layer.Dense(in_size, hidden_size, activation_fn));
//
//            else
//
//                model.add(new K_Layer.Dense(hidden_sizes[ctr - 1], hidden_size, activation_fn));
//
//        }
//
//        model.add(new K_Layer.Dense(hidden_sizes[hidden_sizes.length-1], out_size, activation_fn));
//
//        return model;
//
//    }

    // as List<Object> reserved for Generic_Model. (conflicting java sign.)


    // Recurrent

    static ArrayList<K_Layer.LSTM> LSTM(int in_size, int[] hidden_sizes, int out_size) {

        ArrayList<K_Layer.LSTM> model = new ArrayList<>();

        int ctr = -1;
        for (int hidden_size : hidden_sizes) {
            ctr++;

            if (ctr == 0)

                model.add(new K_Layer.LSTM(in_size, hidden_size));

            else

                model.add(new K_Layer.LSTM(hidden_sizes[ctr-1], hidden_size));

        }

        model.add(new K_Layer.LSTM(hidden_sizes[hidden_sizes.length-1], out_size));

        return model;

    }

    static K_Tensor[] propogate(ArrayList<K_Layer.LSTM> model, K_Tensor[] incoming) {

        K_Tensor[] response = new K_Tensor[incoming.length];

        int ctr = -1;
        for (K_Tensor timestep : incoming) {
            ctr++;

            for (K_Layer.LSTM layer : model)

                timestep = K_Layer.propogate(layer, timestep);

            response[ctr] = timestep;

        }

        return response;

    }

    static ArrayList<K_Tensor> propogate_seq2seq(ArrayList<K_Layer.LSTM> encoder, ArrayList<K_Layer.LSTM> decoder, K_Tensor[] incoming) {

        ArrayList<K_Tensor> response = new ArrayList<>();

        for (K_Tensor timestep : incoming) {

            for (K_Layer.LSTM layer : encoder)

                timestep = K_Layer.propogate(layer, timestep);

            response.add(timestep); // TODO : do all of this (someday)

        }

        return response;

    }

    static ArrayList<Float[][][]> collect_grads(ArrayList<K_Layer.LSTM> model) {

        ArrayList<Float[][][]> layers_grads = new ArrayList<>();

        for (K_Layer.LSTM layer : model) {

            layers_grads.add(new Float[][][]{

                    layer.wf1.grad, layer.wf2.grad,
                    layer.wk1.grad, layer.wk2.grad,
                    layer.wi1.grad, layer.wi2.grad,
                    layer.ws1.grad, layer.ws2.grad,

            });

        }

        return layers_grads;

    }

    static void apply_grads(ArrayList<K_Layer.LSTM> model, ArrayList<Float[][][]> grads) {

        Float[][][] layer_grads;

        int ctr = -1;
        for (K_Layer.LSTM layer : model) {
            ctr++;

            layer_grads = grads.get(ctr);

            layer.wf1.grad = K_Math.add(layer.wf1.grad, layer_grads[0]);
            layer.wf2.grad = K_Math.add(layer.wf2.grad, layer_grads[1]);

            layer.wk1.grad = K_Math.add(layer.wk1.grad, layer_grads[2]);
            layer.wk2.grad = K_Math.add(layer.wk2.grad, layer_grads[3]);

            layer.wi1.grad = K_Math.add(layer.wi1.grad, layer_grads[4]);
            layer.wi2.grad = K_Math.add(layer.wi2.grad, layer_grads[5]);

            layer.ws1.grad = K_Math.add(layer.ws1.grad, layer_grads[6]);
            layer.ws2.grad = K_Math.add(layer.ws2.grad, layer_grads[7]);

        }

    }

    static void learn_from_grads(ArrayList<K_Layer.LSTM> model, float learning_rate) {

        for (K_Layer.LSTM layer : model) {

            layer.wf1.matrix = K_Math.sub(layer.wf1.matrix, K_Math.mul_scalar(learning_rate, layer.wf1.grad));
            layer.wf2.matrix = K_Math.sub(layer.wf2.matrix, K_Math.mul_scalar(learning_rate, layer.wf2.grad));

            layer.wk1.matrix = K_Math.sub(layer.wk1.matrix, K_Math.mul_scalar(learning_rate, layer.wk1.grad));
            layer.wk2.matrix = K_Math.sub(layer.wk2.matrix, K_Math.mul_scalar(learning_rate, layer.wk2.grad));

            layer.wi1.matrix = K_Math.sub(layer.wi1.matrix, K_Math.mul_scalar(learning_rate, layer.wi1.grad));
            layer.wi2.matrix = K_Math.sub(layer.wi2.matrix, K_Math.mul_scalar(learning_rate, layer.wi2.grad));

            layer.ws1.matrix = K_Math.sub(layer.ws1.matrix, K_Math.mul_scalar(learning_rate, layer.ws1.grad));
            layer.ws2.matrix = K_Math.sub(layer.ws2.matrix, K_Math.mul_scalar(learning_rate, layer.ws2.grad));

        }

    }

    static void clear_grads(ArrayList<K_Layer.LSTM> model) {

        for (K_Layer.LSTM layer : model)

            K_Layer.clear_grad(layer);

    }

    static void clear_states(ArrayList<K_Layer.LSTM> model) {

        for (K_Layer.LSTM layer : model)

            K_Layer.clear_state(layer);

    }
    
    
    // Generic FNs
    

    static K_Tensor[] propogate(List<Object> model, K_Tensor[] incoming) {

        K_Tensor[] response = new K_Tensor[incoming.length];

        int ctr = -1;
        for (K_Tensor timestep : incoming) {
            ctr++;

            for (Object layer : model)

                if (layer instanceof K_Layer.Dense)

                    timestep = K_Layer.propogate((K_Layer.Dense) layer, timestep);

                else if (layer instanceof K_Layer.LSTM)

                    timestep = K_Layer.propogate((K_Layer.LSTM) layer, timestep);

            response[ctr] = timestep;

        }

        return response;

    }

    static ArrayList<Float[][][]> collect_grads(List<Object> model) {

        ArrayList<Float[][][]> layers_grads = new ArrayList<>();

        for (Object layer : model) {

            if (layer instanceof K_Layer.Dense) {

                layers_grads.add(new Float[][][]{((K_Layer.Dense) layer).w.grad});

            } else if (layer instanceof K_Layer.LSTM) {

                K_Layer.LSTM layer_ = (K_Layer.LSTM) layer;
                
                layers_grads.add(new Float[][][]{

                        layer_.wf1.grad, layer_.wf2.grad,
                        layer_.wk1.grad, layer_.wk2.grad,
                        layer_.wi1.grad, layer_.wi2.grad,
                        layer_.ws1.grad, layer_.ws2.grad,

                });

            }

        }
        
        return layers_grads;

    }

    static void apply_grads(List<Object> model, ArrayList<Float[][][]> grads) {

        Float[][][] layer_grads;

        int ctr = -1;
        for (Object layer : model) {
            ctr++;

            if (layer instanceof K_Layer.Dense) {

                K_Layer.Dense layer_ = (K_Layer.Dense) layer;

                layer_.w.grad = K_Math.add(layer_.w.grad, grads.get(ctr)[0]);

            } else if (layer instanceof K_Layer.LSTM) {

                K_Layer.LSTM layer_ = (K_Layer.LSTM) layer;

                layer_grads = grads.get(ctr);

                layer_.wf1.grad = K_Math.add(layer_.wf1.grad, layer_grads[0]);
                layer_.wf2.grad = K_Math.add(layer_.wf2.grad, layer_grads[1]);

                layer_.wk1.grad = K_Math.add(layer_.wk1.grad, layer_grads[2]);
                layer_.wk2.grad = K_Math.add(layer_.wk2.grad, layer_grads[3]);

                layer_.wi1.grad = K_Math.add(layer_.wi1.grad, layer_grads[4]);
                layer_.wi2.grad = K_Math.add(layer_.wi2.grad, layer_grads[5]);

                layer_.ws1.grad = K_Math.add(layer_.ws1.grad, layer_grads[6]);
                layer_.ws2.grad = K_Math.add(layer_.ws2.grad, layer_grads[7]);

            }

        }

    }

    static void learn_from_grads(List<Object> model, float learning_rate) {

        for (Object layer : model)

            if (layer instanceof K_Layer.Dense) {

                K_Layer.Dense layer_ = (K_Layer.Dense) layer;

                layer_.w.matrix = K_Math.sub(layer_.w.matrix, K_Math.mul_scalar(learning_rate, layer_.w.grad));

            }

            else if (layer instanceof K_Layer.LSTM) {

                K_Layer.LSTM layer_ = (K_Layer.LSTM) layer;

                layer_.wf1.matrix = K_Math.sub(layer_.wf1.matrix, K_Math.mul_scalar(learning_rate, layer_.wf1.grad));
                layer_.wf2.matrix = K_Math.sub(layer_.wf2.matrix, K_Math.mul_scalar(learning_rate, layer_.wf2.grad));

                layer_.wk1.matrix = K_Math.sub(layer_.wk1.matrix, K_Math.mul_scalar(learning_rate, layer_.wk1.grad));
                layer_.wk2.matrix = K_Math.sub(layer_.wk2.matrix, K_Math.mul_scalar(learning_rate, layer_.wk2.grad));

                layer_.wi1.matrix = K_Math.sub(layer_.wi1.matrix, K_Math.mul_scalar(learning_rate, layer_.wi1.grad));
                layer_.wi2.matrix = K_Math.sub(layer_.wi2.matrix, K_Math.mul_scalar(learning_rate, layer_.wi2.grad));

                layer_.ws1.matrix = K_Math.sub(layer_.ws1.matrix, K_Math.mul_scalar(learning_rate, layer_.ws1.grad));
                layer_.ws2.matrix = K_Math.sub(layer_.ws2.matrix, K_Math.mul_scalar(learning_rate, layer_.ws2.grad));
                
            }

    }

    static void clear_grads(List<Object> model) {

        for (Object layer : model)

            if (layer instanceof K_Layer.Dense)

                K_Layer.clear_grad((K_Layer.Dense) layer);

            else if (layer instanceof K_Layer.LSTM)

                K_Layer.clear_grad((K_Layer.LSTM) layer);

    }
    

}


class K_Api{


    static Object[] loss_and_grad_from_datapoint(ArrayList<K_Layer.LSTM> model, ArrayList<Float[][]> datapoint) {

        K_Tensor[] datapoint_t = new K_Tensor[datapoint.size()];

        int ctr = -1;
        for (Float[][] timestep : datapoint) {
            ctr++;

            datapoint_t[ctr] = new K_Tensor(timestep);

        }

        K_Tensor[] response = K_Model.propogate(model, datapoint_t);

        float loss = K_Tensor.fill_grads(K_Util.sequence_loss(datapoint_t, response));

        ArrayList<Float[][][]> grads = K_Model.collect_grads(model);

        return new Object[]{loss, grads};

    }

    static Object[] loss_and_grad_from_batch(ArrayList<K_Layer.LSTM> model, ArrayList<ArrayList<Float[][]>> batch) {

        float batch_loss = 0;

        ArrayList<Float[][][]> batch_grad = K_Model.collect_grads(model);

        for (ArrayList<Float[][]> sample : batch) {

            Object[] result = loss_and_grad_from_datapoint(K_Util.copy(model), sample);

            // parallelize here.



            batch_loss += (float) result[0]; // TODO : this section comes after parallel

            int ctr = -1;
            for (Float[][][] layer_grad : (ArrayList<Float[][][]>) result[1]) {
                ctr++;

                Float[][][] batch_grad_layer = batch_grad.get(ctr);

                int ctr2 = -1;
                for (Float[][] weight_grad : layer_grad) {
                    ctr2++;

                    batch_grad_layer[ctr] = K_Math.add(weight_grad, batch_grad_layer[ctr2]);

                }

            }

        }

        return new Object[]{batch_loss, batch_grad};

    }


    static float train_on_batch(ArrayList<K_Layer.LSTM> model, ArrayList<ArrayList<Float[][]>> batch, float learning_rate) {

        Object[] result = loss_and_grad_from_batch(model, batch);

        float batch_loss = (float) result[0];

        ArrayList<Float[][][]> batch_grad = (ArrayList<Float[][][]>) result[1];

        K_Model.apply_grads(model, batch_grad);

        K_Model.learn_from_grads(model, learning_rate/batch.size());

        K_Model.clear_grads(model);
        // K_Model.clear_states(model);

        return batch_loss;

    }

    static float train_on_dataset(ArrayList<K_Layer.LSTM> model, ArrayList<ArrayList<Float[][]>> dataset, int batch_size, float learning_rate) {

        float ep_loss = 0;

        for (ArrayList<ArrayList<Float[][]>> batch : K_Util.batchify(K_Util.shuffle(dataset), batch_size))

            ep_loss += K_Api.train_on_batch(model, batch, learning_rate);

        return ep_loss;

    }

    static void train_on_dataset(ArrayList<K_Layer.LSTM> model, ArrayList<ArrayList<Float[][]>> dataset, int batch_size, float learning_rate, int hm_epochs) {

        float ep_loss;

        for (int i = 0; i < hm_epochs; i++) {

            ep_loss = 0;

            for (ArrayList<ArrayList<Float[][]>> batch : K_Util.batchify(K_Util.shuffle(dataset), batch_size))

                ep_loss += K_Api.train_on_batch(model, batch, learning_rate);

            System.out.println("Epoch " + i + " Loss " + ep_loss);

        }

    }

    static void train_on_dataset(ArrayList<K_Layer.LSTM> model, ArrayList<ArrayList<Float[][]>> dataset, float train_ratio, float test_ratio, int batch_size, float learning_rate, int hm_epochs, int test_per_epochs) {

        assert train_ratio + test_ratio <= 1;

        // TODO : split dataset here.

        // & start displaying train & ep loss

        float ep_loss;

        for (int i = 0; i < hm_epochs; i++) {

            ep_loss = 0;

            for (ArrayList<ArrayList<Float[][]>> batch : K_Util.batchify(K_Util.shuffle(dataset), batch_size))

                ep_loss += K_Api.train_on_batch(model, batch, learning_rate);

            System.out.println("Epoch " + i + "\n\tTrain Loss: " + ep_loss + ((i + 1) % test_per_epochs == 0 ? "\n\tTest Loss: " + calc_test_loss() : ""));

        }

    }

    static float calc_test_loss() { return .0f; } // todo: do


    // Generic Modelling


    static List<Object> Generate_Generic_Model(int[] sizes, String[] layer_types, String dense_act_fn) {

        assert layer_types.length == sizes.length-1;

        List<Object> layers = new ArrayList<>();

        int ctr = -1;
        for (String layer_type : layer_types) {
            ctr++;

            switch(layer_type) {

                case "dense":

                    layers.add(new K_Layer.Dense(sizes[ctr],sizes[ctr+1],dense_act_fn));

                    break;

                case "lstm":

                    layers.add(new K_Layer.LSTM(sizes[ctr],sizes[ctr+1]));

                    break;

                default:
                    System.out.println("Available layer types: dense/lstm");

            }

        }

        return layers;

    } ; static List<Object> Generate_Generic_Model(int[] sizes, String[] layer_types) { return Generate_Generic_Model(sizes, layer_types, "relu"); }


    static Object[] loss_and_grad_from_datapoint(List<Object> model, ArrayList<Float[][]> datapoint) {

        K_Tensor[] datapoint_t = new K_Tensor[datapoint.size()];

        int ctr = -1;
        for (Float[][] timestep : datapoint) {
            ctr++;

            datapoint_t[ctr] = new K_Tensor(timestep);

        }

        K_Tensor[] response = K_Model.propogate(model, datapoint_t);

        float loss = K_Tensor.fill_grads(K_Util.sequence_loss(datapoint_t, response));

        ArrayList<Float[][][]> grads = K_Model.collect_grads(model);

        return new Object[]{loss, grads};

    }

    static Object[] loss_and_grad_from_batch(List<Object> model, ArrayList<ArrayList<Float[][]>> batch) {

        float batch_loss = 0;

        ArrayList<Float[][][]> batch_grad = K_Model.collect_grads(model);

        for (ArrayList<Float[][]> sample : batch) {

            Object[] result = loss_and_grad_from_datapoint(K_Util.copy(model), sample);

            // parallelize here.



            batch_loss += (float) result[0]; // TODO : this section comes after parallel

            int ctr = -1;
            for (Float[][][] layer_grad : (ArrayList<Float[][][]>) result[1]) {
                ctr++;

                Float[][][] batch_grad_layer = batch_grad.get(ctr);

                int ctr2 = -1;
                for (Float[][] weight_grad : layer_grad) {
                    ctr2++;

                    batch_grad_layer[ctr] = K_Math.add(weight_grad, batch_grad_layer[ctr2]);

                }

            }

        }

        return new Object[]{batch_loss, batch_grad};

    }


    static float train_on_batch(List<Object> model, ArrayList<ArrayList<Float[][]>> batch, float learning_rate) {

        Object[] result = loss_and_grad_from_batch(model, batch);

        float batch_loss = (float) result[0];

        ArrayList<Float[][][]> batch_grad = (ArrayList<Float[][][]>) result[1];

        K_Model.apply_grads(model, batch_grad);

        K_Model.learn_from_grads(model, learning_rate/batch.size());

        K_Model.clear_grads(model);

        return batch_loss;

    }

    static float train_on_dataset(List<Object> model, ArrayList<ArrayList<Float[][]>> dataset, int batch_size, float learning_rate) {

        float ep_loss = 0;

        for (ArrayList<ArrayList<Float[][]>> batch : K_Util.batchify(K_Util.shuffle(dataset), batch_size))

            ep_loss += K_Api.train_on_batch(model, batch, learning_rate);

        return ep_loss;

    }

    static void train_on_dataset(List<Object> model, ArrayList<ArrayList<Float[][]>> dataset, int batch_size, float learning_rate, int hm_epochs) {

        float ep_loss;

        for (int i = 0; i < hm_epochs; i++) {

            ep_loss = 0;

            for (ArrayList<ArrayList<Float[][]>> batch : K_Util.batchify(K_Util.shuffle(dataset), batch_size))

                ep_loss += K_Api.train_on_batch(model, batch, learning_rate);

            System.out.println("Epoch " + i + " Loss " + ep_loss);

        }

    }

    static void train_on_dataset(List<Object> model, ArrayList<ArrayList<Float[][]>> dataset, float train_ratio, float test_ratio, int batch_size, float learning_rate, int hm_epochs, int test_per_epochs) {

        assert train_ratio + test_ratio <= 1;

        // TODO : split dataset here.

        // & start displaying train & ep loss

        float ep_loss;

        for (int i = 0; i < hm_epochs; i++) {

            ep_loss = 0;

            for (ArrayList<ArrayList<Float[][]>> batch : K_Util.batchify(K_Util.shuffle(dataset), batch_size))

                ep_loss += K_Api.train_on_batch(model, batch, learning_rate);

            System.out.println("Epoch " + i + "\n\tTrain Loss: " + ep_loss + ((i + 1) % test_per_epochs == 0 ? "\n\tTest Loss: " + calc_test_loss() : ""));

        }

    }

    //static float calc_test_loss() { return .0f; } // todo: do


}


class K_Util {


    static void xavierize(List<K_Layer.Dense> model) {

//        layer.w = ...; // TODO : do

    }

    static void xavierize(ArrayList<K_Layer.LSTM> model) {

//        layer.w = ...; // TODO : do

    }

    static ArrayList<ArrayList<Float[][]>> shuffle(ArrayList<ArrayList<Float[][]>> items) {

        Collections.shuffle(items);

        return items;

    }

    static ArrayList<ArrayList<ArrayList<Float[][]>>> batchify(ArrayList<ArrayList<Float[][]>> dataset, int batch_size) {

        ArrayList<ArrayList<ArrayList<Float[][]>>> batches = new ArrayList<>();

        ArrayList<ArrayList<Float[][]>> batch;

        for (int i = 0; i < dataset.size()/batch_size; i++) {

            batch = new ArrayList<>();

            for (int j = 0; j < batch_size; j++)

                batch.add(dataset.get(i*batch_size+j));

            batches.add(batch);

        }

        return batches;

    }

    static K_Tensor sequence_loss(K_Tensor[] sample, K_Tensor[] response, String type) {

        K_Tensor loss = null; // K_Tensor.zeros(K_Tensor.size(response[0]));

        if (type.equals("1")) {

            for (int t = 0; t < response.length - 1; t++)

                if (t == 0)

                    loss = K_Tensor.mean_square(response[t], sample[t + 1]);

                else

                    loss = K_Tensor.add(loss, K_Tensor.mean_square(response[t], sample[t + 1]));

        }

        else { return null; } // todo : seq2seq

        return loss;

    } static K_Tensor sequence_loss(K_Tensor[] sample, K_Tensor[] response) { return sequence_loss(sample, response, "1"); }


    // Extra


    static ArrayList<K_Layer.LSTM> copy(ArrayList<K_Layer.LSTM> model) {

        ArrayList<K_Layer.LSTM> model_copy = new ArrayList<>();

        for (K_Layer.LSTM layer : model)

            model_copy.add(new K_Layer.LSTM(layer));

        return model_copy;

    }

    static List<Object> copy(List<Object> model) {

        List<Object> model_copy = new ArrayList<>();

        for (Object layer : model)

            if (layer instanceof K_Layer.Dense)

                model_copy.add(new K_Layer.Dense((K_Layer.Dense) layer));

            else if (layer instanceof K_Layer.LSTM)

                model_copy.add(new K_Layer.LSTM((K_Layer.LSTM) layer));

        return model_copy;

    }


}