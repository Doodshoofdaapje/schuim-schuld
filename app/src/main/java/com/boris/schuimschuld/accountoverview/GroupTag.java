package com.boris.schuimschuld.accountoverview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.boris.schuimschuld.R;
import com.boris.schuimschuld.account.Group;

public class GroupTag extends ConstraintLayout {

    private Group group;

    public GroupTag(Context context, Group group) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.tag_group, this);

        this.group = group;

        TextView textGroupName = (TextView) findViewById(R.id.tagGroupName);
        textGroupName.setText(group.toString());
    }

    public Group getGroup() {
        return this.group;
    }
}
