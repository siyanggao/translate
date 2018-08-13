package com.gsy.translate.home;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.okhttp.CommonOkHttpClient;
import com.gsy.okhttp.DisposeDataListener;
import com.gsy.translate.R;
import com.gsy.translate.bean.DBWord;
import com.gsy.translate.bean.TranslateWord;
import com.gsy.translate.databinding.FragmentWordTranslateBinding;
import com.gsy.translate.home.adapter.TranslateAdapter;
import com.gsy.translate.home.bean.TranslateWordBean;
import com.gsy.translate.utils.Constants;
import com.gsy.translate.utils.CustomToast;
import com.gsy.translate.utils.DataBaseManager;
import com.gsy.translate.utils.Tools;
import com.gsy.translate.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

/**
 * Created by Think on 2018/2/4.
 */

public class WordTranslateFragment extends DialogFragment {

    FragmentWordTranslateBinding mBinding;
    List<DBWord> searchData;
    List<DBWord> historyData;
    TranslateAdapter searchAdapter;
    TranslateAdapter historyAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    SQLiteDatabase sqLiteDatabase;
    @Override
    public void onStart()
    {
        super.onStart();
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics( dm );
        getDialog().getWindow().setLayout( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getDialog().getWindow().setGravity(Gravity.TOP);
        getDialog().setCanceledOnTouchOutside(true);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_fragment_style);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_word_translate,container,false);
        sharedPreferences = getActivity().getSharedPreferences("myconfig", Context.MODE_PRIVATE);
        sqLiteDatabase = DataBaseManager.with(getActivity()).getDataBase();
        initView();

        return mBinding.getRoot();
    }

    private void initView() {
        mBinding.setPresenter(new Presenter());
        searchData = new ArrayList<>();
        searchAdapter = new TranslateAdapter(getActivity(),searchData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mBinding.wordTranslateSearch.setLayoutManager(linearLayoutManager);
        mBinding.wordTranslateSearch.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mBinding.wordTranslateSearch.setAdapter(searchAdapter);
        searchAdapter.setmOnItemClickListener(new TranslateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                search(searchData.get(position));
            }
        });

        historyData = new ArrayList<>();
        historyAdapter = new TranslateAdapter(getActivity(),historyData);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        mBinding.wordTranslateHistory.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mBinding.wordTranslateHistory.setLayoutManager(linearLayoutManager2);
        mBinding.wordTranslateHistory.setAdapter(historyAdapter);
        String historySet = sharedPreferences.getString("searchHistory",null);
        if(historySet!=null){
            String[] historyArray = historySet.split("\\|");
            for (int i = 0; i < historyArray.length; i++) {
                //DBWord dbWord = queryEqare(historyArray[i]);
                String[] dbWordArray = historyArray[i].split("-");
                DBWord dbWord = new DBWord(dbWordArray[0],dbWordArray[1]);
                if(dbWord!=null){
                    historyData.add(dbWord);
                }
            }
        }
        historyAdapter.notifyDataSetChanged();
        if(historyData.size()>0)
            mBinding.wordTranslateClear.setVisibility(View.VISIBLE);
        //弹出软键盘
        mBinding.wordTranslateEt.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
        historyAdapter.setmOnItemClickListener(new TranslateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                search(historyData.get(position));
            }
        });

        mBinding.wordTranslateEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    search(new DBWord(v.getText().toString(),null));
                }
                return false;
            }
        });
    }

    void search(final DBWord dbWord){
        mBinding.wordTranslateDetail.setVisibility(View.VISIBLE);
        mBinding.wordTranslateSearch.setVisibility(View.GONE);
        mBinding.wordTranslateHistory.setVisibility(View.GONE);
        mBinding.wordTranslateClear.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBinding.wordTranslateEt.getWindowToken(),0);
        if(dbWord.getDefinition()!=null) changeHistory(dbWord);
        //网络获取
        //如果正确 dbWord.getDefinition()==null，changeHistory(dbword)
        CommonOkHttpClient.get(Constants.baseUrlDict+"/v1/translate")
                .addParam("word",dbWord.getWord())
                .addListener(new DisposeDataListener<TranslateWordBean>(){
                    @Override
                    public void onSuccess(TranslateWordBean responseObj) {
                        if(responseObj.getCode()==1){
                            TranslateWord word = responseObj.getData().getBasic().get(0);
                            mBinding.wordTranslateDetailWord.setText(word.getWord());
                            //String definition = "";
                            SpannableStringBuilder style = new SpannableStringBuilder();
                            for(int i=0;i<word.getDefinition().size();i++){
//                                if(definition.length()>0) definition+="\n";
//                                definition+=item;
                                String item = word.getDefinition().get(i);
                                if(i!=0) item="\n"+item;
                                SpannableStringBuilder styleItem = new SpannableStringBuilder(item);
                                styleItem.setSpan(new ForegroundColorSpan(Color.BLACK), 0, item.indexOf(" "), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                styleItem.setSpan(new TypefaceSpan("monospace"), 0, item.indexOf(" "), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                style.append(styleItem);
                            }
                            mBinding.wordTranslateDetailDefinition.setText(style);
                            String note = "";
                            for(String item:word.getNote()){
                                if(note.length()>0) note+="\n";
                                note+=item;
                            }
                            mBinding.wordTranslateDetailNote.setText(note);
                            //获取数据后，修改历史记录
                            if(dbWord.getDefinition()==null){
                                dbWord.setDefinition(responseObj.getData().getBasic().get(0).getDefinition().get(0));
                                changeHistory(dbWord);
                            }
                        }else{
                            CustomToast.makeText(getActivity(),responseObj.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception resonObj) {
                        CustomToast.makeText(getActivity(),resonObj.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).exec();

    }
    void changeHistory(DBWord queryWord){
        if(editor==null)
            editor = sharedPreferences.edit();
        historyData.remove(queryWord);
        historyData.add(0,queryWord);
        String historySet = "";
        int minValue = historyData.size()>20?20:historyData.size();
        for (int i=0;i<minValue;i++){
            historySet +=(historyData.get(i).getWord()+"-"+historyData.get(i).getDefinition()+"|");
        }
        historySet = historySet.substring(0,historySet.length()-1);
        editor.putString("searchHistory",historySet);
        editor.commit();
    }

    List<DBWord> queryLike(String word){
        List<DBWord> dbWords = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select word,definition from word where word like '"+word+"%' limit 0,20",null);
        cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            cursor.move(i);
            dbWords.add(new DBWord(cursor.getString(0),cursor.getString(1)));
        }
        return dbWords;
    }
    DBWord queryEqare(String word){
        List<DBWord> dbWords = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select word,definition from word where word = '"+word+"'",null);
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            return new DBWord(cursor.getString(0),cursor.getString(1));
        }else{
            return null;
        }
    }
    public class Presenter{
        public void cancel(View v){
            WordTranslateFragment.this.dismiss();
        }
        public void clearHistory(View v){
            if(editor==null)
                editor = sharedPreferences.edit();
            editor.remove("searchHistory");
            editor.commit();
            historyData.clear();
            historyAdapter.notifyDataSetChanged();
            mBinding.wordTranslateClear.setVisibility(View.GONE);
        }
        public TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBinding.wordTranslateDetail.setVisibility(View.GONE);
                String word = s.toString();
                if(word!=null&&word.length()>0){//搜索
                    mBinding.wordTranslateSearch.setVisibility(View.VISIBLE);
                    mBinding.wordTranslateHistory.setVisibility(View.GONE);
                    mBinding.wordTranslateClear.setVisibility(View.GONE);

                    searchAdapter.data.clear();
                    searchAdapter.data.addAll(queryLike(word));

                    searchAdapter.notifyDataSetChanged();
                }else{//历史记录
                    mBinding.wordTranslateSearch.setVisibility(View.GONE);
                    mBinding.wordTranslateHistory.setVisibility(View.VISIBLE);
                    if(historyData.size()>0)
                        mBinding.wordTranslateClear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }


}
