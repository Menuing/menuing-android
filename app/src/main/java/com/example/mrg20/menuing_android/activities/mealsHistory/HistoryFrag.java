package com.example.mrg20.menuing_android.activities.mealsHistory;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mrg20.menuing_android.R;
//calendar pot molar per despres
import java.util.Calendar;

public class HistoryFrag extends Fragment implements AdapterView.OnItemClickListener {
    //llista que contindra les receptes
    private ListView list;
    //aixo es pel menu contextual
    ActionClass action;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //action per al longClick
        if (savedInstanceState != null){
            action = new ActionClass(getActivity(), savedInstanceState.getInt("pos"));
        } else {
            action = new ActionClass(getActivity(), -1);
        }

        //Aixo es la listView a on si guardaran les dades
        list = (ListView) getActivity().findViewById(R.id.listDB);

        //Aixo es el cursor sobre la base de dades que conte les dades sobre les receptes
        //Cursor c = db.rawQuery("SELECT * FROM Recipes WHERE name=?", args);
        Cursor c = null;

        //Agafem les id dels valors que te cada instancia de la base de dades
        int [] to = new int [] {R.id.elementLayoutTxt};
        String [] from = new String[] {"recipeName"};

        //Apliquem el simpleCursorAdapter sobre el que tenim i resem per a que tiri
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), R.layout.history_element_layout, c, from, to, 1);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(action);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(action);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.history_list_frag, container, false);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity().getApplicationContext(), "CLICK :D", Toast.LENGTH_LONG).show();
    }

    private class ActionClass implements AdapterView.OnItemLongClickListener, AbsListView.MultiChoiceModeListener{
        private Object mActionMode = null;
        private Activity activity;
        private int position;

        public ActionClass(Activity activity, int position){
            this.activity = activity;
            this.position = position;
        }

        // SI FEM UN LONG CLICK SOBRE UN ELEMENT DE LA LLISTA OBRIM MENU CONTEXTUAL
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (mActionMode != null){
                return false;
            }

            mActionMode = activity.startActionMode(this);
            view.setSelected(true);
            return true;
        }

        @Override
        public void onItemCheckedStateChanged(android.view.ActionMode mode, int position, long id, boolean checked) {
            mode.setTag(position);
        }

        //OBRIM EL MENU CONTEXTUAL I SELECCIONEM L'ACCIO A FER
        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.history_contextmenu, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false;
        }

        //AQUEST METODE ES EL QUE APLICA L'ACCIO SELECCIONADA SOBRE LA BD (QUAN LA TINGUIM)
        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            mActionMode = null;
        }
    }
}
