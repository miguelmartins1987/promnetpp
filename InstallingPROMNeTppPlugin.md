# Contents #


# Introduction #

Although not strictly necessary to use PROMNeT++, the plug-in allows for direct translation inside the OMNeT++ IDE. This guide currently illustrates its installation procedure under a Windows environment; however, installation under any other environment should be very similar, if not practically equal.

# Method 1 - Installing the plugin via manual download #

  1. Head over to the <a href='https://code.google.com/p/promnetpp/downloads/list'><i>Downloads</i></a> page and download the latest _promnetpp-plugin-x.y.z.zip_ file, where _x.y.z_ corresponds to the latest version number. _Do not extract the zip file's contents, as it is unecessary._
  1. Open the OMNeT++ IDE, then select Help|Install New Software...<br><br><img src='https://promnetpp.googlecode.com/svn/media/InstallNewSoftware.png' />
<ol><li>Click on the <i>Add...</i> button located to the right of the Available Software Sites dropdown box.<br><br><img src='https://promnetpp.googlecode.com/svn/media/AvailableSoftwareSites.png' />
</li><li>We will be adding the plugin's "repository" directly from the zip archive. Do this by clicking on the <i>Archive...</i> button, browsing to the zip file's location, and selecting it.<br><br><img src='https://promnetpp.googlecode.com/svn/media/ArchiveButton.png' /><br><br><img src='https://promnetpp.googlecode.com/svn/media/BrowseToZipFile.png' />
</li><li>The <i>Location</i> field should now point to the directory where the archive selected in the previous step is located. It is always preceded by  <i>jar:file:</i> and is delimited by an exclamation mark (!) at the end. An example of this would be <i>jar:file:/C:/tmp/promnetpp-plugin-0.0.1.zip!/</i>, as per the screenshot above. Click <i>OK</i> to confirm.<br><br><img src='https://promnetpp.googlecode.com/svn/media/ArchiveLocationClickOK.png' />
</li><li>If done correctly, an entry for the plugin should appear. Confirm that you want to install the plugin by marking the entry's checkbox and clicking on the <i>Next ></i> button to proceed to the next page.<br>
<ul><li>If no entry for the plugin shows up, try unchecking the box labelled <i>Group items by category</i><br>.<br><img src='https://promnetpp.googlecode.com/svn/media/MarkTheCheckboxAndClickNext.png' />
</li></ul></li><li>You should be on the <i>Install Details</i> page at this point, as illustrated below. For the vast majority (if not for all) of the users, there should be no need to actually check out the plugin's details. Proceed to the next page by, once again, clicking on the <i>Next ></i> button.<br><br><img src='https://promnetpp.googlecode.com/svn/media/InstallDetailsClickNext.png' />
</li><li>Review the enclosed license presented on the <i>Review Licenses</i> page, if needed. Naturally, like any other plugin for Eclipse (or, in this case, the OMNeT++ IDE, which is Eclipse-based), you must accept the terms of the license agreement in order to install and use this plugin. Confirm that you want to install the plugin by clicking on <i>I accept the terms of the license agreement</i>, and clicking on the <i>Finish</i> button thereafter.<br><br><img src='https://promnetpp.googlecode.com/svn/media/AcceptTermsAndClickFinish.png' />
</li><li>In the event that a <i>Security Warning</i> pops up, ignore it by clicking <i>OK</i>. This is to be expected, as the plug-in's content has not yet been signed (as of this guide's time of writing).<br>
</li><li>Restart the OMNeT++ IDE when prompted, and the plug-in will be ready for use.<br><br><img src='https://promnetpp.googlecode.com/svn/media/RestartOmnetpp.png' /></li></ol>

<h1>Method 2 - Installing the plugin via update site</h1>
See <a href='InstallingPROMNeTppPluginUpdateSite.md'>this article</a>.<br>
<br>
<h1>Plug-in installed. Now how do I use it?</h1>

See <a href='UsingPROMNeTppPlugin.md'>this article</a> for instructions on how to use the plug-in.