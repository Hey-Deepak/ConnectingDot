package com.ts.connectingdot.feature.newGroupChat.comp

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ts.connectingdot.domain.model.User
import com.ts.connectingdot.domain.model.ext.id
import com.ts.connectingdot.helper.navigateTo
import com.ts.connectingdot.ui.Screens
import com.ts.connectingdot.ui.comp.UserCard


fun LazyListScope.MembersInput(userList: List<User>, members: SnapshotStateList<String>) {

    item {
        Text(
            modifier = Modifier
                .padding(start = 16.dp)
                .padding(vertical = 10.dp)
                .padding(bottom = 8.dp),
            text = "Memebers",
            style = MaterialTheme.typography.titleMedium
        )
    }

    items(userList) { user ->
        UserCard(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 12.dp),
            user = user,
            checked = members.contains(user.id()),
            onCheckedChanged = { checked ->
                if (checked) members.add(user.id()) else members.remove(user.id())
            },
            onClick = {
                if (members.contains(user.id())) {
                    members.remove(user.id())
                } else {
                    members.add(user.id())
                }
            })
    }

}
