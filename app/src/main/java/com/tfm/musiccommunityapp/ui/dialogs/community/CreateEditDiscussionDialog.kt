package com.tfm.musiccommunityapp.ui.dialogs.community

import androidx.fragment.app.DialogFragment
import com.tfm.musiccommunityapp.domain.model.DiscussionDomain

class CreateEditDiscussionDialog(
    private val discussion: DiscussionDomain?,
    private val onSaveClicked: (DiscussionDomain) -> Unit
) : DialogFragment(){


}