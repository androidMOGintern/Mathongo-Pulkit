private void fetchData() {

        if(isConnected()) {

            pd.setMessage("Loading");
            pd.show();

            class11_MaterialsFormula.clear();
            final Class11_Formula class11_formula = new Class11_Formula();
            final StringBuilder sb = new StringBuilder();


            AndroidNetworking.get(Api_Urls.BASE_URL + "authorize/resources")
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {

                                JSONObject formula = response.getJSONObject("Formula");
                                JSONArray class11Materials = formula.getJSONArray("Class_11");

                                for (int i = 0; i < class11Materials.length(); ++i) {

                                    String mName = class11Materials.getString(i);

                                    Material material = new Material();
                                    material.setCategory_Level_I("Formula");
                                    material.setCategory_Level_II("Class XI");
                                    sb.append(mName + ",");
                                    material.setName(mName);

                                    class11_MaterialsFormula.add(material);
                                }

                                class11_formula.setStoredInDB(sb.toString());
                                SugarRecord.save(class11_formula);
                                pd.hide();

                                adapter.notifyDataSetChanged();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        }else{

            List<Class11_Formula> listStored = listAll(Class11_Formula.class);

            if(listStored != null && listStored.size() > 0){

                Class11_Formula object = listStored.get(0);
                class11_MaterialsFormula.clear();

                String mName = object.getStoredInDB();

                String[] offlinePDFs = mName.split(",");

                for (int i = 0; i < offlinePDFs.length ; ++i) {

                    String offName = offlinePDFs[i];


                    Material material = new Material();
                    material.setCategory_Level_I("Formula");
                    material.setCategory_Level_II("Class XI");
                    material.setName(offName);

                    class11_MaterialsFormula.add(material);
                }

                adapter.notifyDataSetChanged();

            }
        }
    }


    public boolean isConnected(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();


        return (activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        unbinder.unbind();
        SugarRecord.deleteAll(Class11_Formula.class);
    }
