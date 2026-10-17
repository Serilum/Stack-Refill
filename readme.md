<h2>Stack Refill</h2>
<p><a href="https://github.com/Serilum/Stack-Refill"><img src="https://workflow.serilum.com/web/logo/128/stack-refill.gif" width="100" height="100"></a></p><h2>Download</h2>
<p>You can download Stack Refill on CurseForge and Modrinth:</p><p>&nbsp;&nbsp;CurseForge: &nbsp;&nbsp;<a href="https://curseforge.com/minecraft/mc-mods/stack-refill">https://curseforge.com/minecraft/mc-mods/stack-refill</a><br>&nbsp;&nbsp;Modrinth: &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://modrinth.com/mod/stack-refill">https://modrinth.com/mod/stack-refill</a></p>
<h2>Issue Tracker</h2>
<p>To keep a better overview of all mods, the issue tracker is located in a separate repository.<br>&nbsp;&nbsp;For issues, ideas, suggestions or anything else, please follow this link:</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;-> <a href="https://serilum.com/url/issue-tracker">Issue Tracker</a></p>
<h2>Pull Requests</h2>
<p>Because of the way mod loader files are bundled into one jar, some extra information is needed to do a PR.<br>&nbsp;&nbsp;A wiki page entry about it is available here:</p>
<p>&nbsp;&nbsp;&nbsp;&nbsp;-> <a href="https://serilum.com/url/pull-requests">Pull Request Information</a></p>
<h2>Mod Description</h2>
<p style="text-align:center"><a href="https://serilum.com/" target="_blank" rel="nofollow"><img src="https://cdn.serilum.com/description/header/header.png" alt="A Serilum Mod. Made with love. Lots of active mods for Fabric, Forge and Neo!" width="838" height="400"></a></p>
<p style="text-align:center"><a href="https://curseforge.com/members/serilum/projects" target="_blank" rel="nofollow"><img src="https://workflow.serilum.com/badges/svg/curseforge.svg" alt="CurseForge" width="200"></a> <a href="https://modrinth.com/user/Serilum" target="_blank" rel="nofollow"><img src="https://workflow.serilum.com/badges/svg/modrinth.svg" alt="Modrinth" width="200"></a> <a href="https://patreon.com/serilum" target="_blank" rel="nofollow"><img src="https://workflow.serilum.com/badges/svg/patreon.svg" alt="Patreon" width="200"></a> <a href="https://youtube.com/@serilum" target="_blank" rel="nofollow"><img src="https://workflow.serilum.com/badges/svg/youtube.svg" alt="YouTube" width="200"></a></p>
<h2><br><img src="https://cdn.serilum.com/description/nameplate/about-the-mod.svg" alt="About the mod" width="838" height="64"></h2>
<p><span style="font-size:18px">Stack Refill is a utility mod which automatically refills the itemstack in the player's hand if there is any of the same item in the inventory. It works for weapons in combat, tools used on blocks, stacks when placing blocks, eating food, using potions, tossing items on the ground, drinking milk buckets, when breaking a fishing rod and should work with all modded blocks. Thrown items like tridents and splash potions are refilled too. One of my personal favourite quality of life tweaks.<br><br>Has to be installed on the <strong>server</strong>, as that's where the refilling happens. Players don't need it on their client. What gets refilled can be changed in the config, including a blacklist and whitelist for specific items. With <em>/stackrefill</em> every player can also turn it off for themselves.</span></p>
<h2><br><br><img src="https://cdn.serilum.com/description/nameplate/dependencies.svg" alt="Dependencies" width="838" height="64"></h2>
<p><a href="https://curseforge.com/minecraft/mc-mods/collective" target="_blank" rel="nofollow"><img src="https://cdn.serilum.com/description/badges/dependencies/requires/collective.svg" alt="Requires Collective" width="251" height="49"></a><br><a href="https://curseforge.com/minecraft/modpacks/the-vanilla-experience" target="_blank" rel="nofollow"><img src="https://cdn.serilum.com/description/badges/dependencies/includes/the-vanilla-experience.svg" alt="Included in The Vanilla Experience" width="350" height="49"></a><a href="https://curseforge.com/minecraft/mc-mods/serilums-qol-bundle" target="_blank" rel="nofollow"><img src="https://cdn.serilum.com/description/badges/dependencies/includes/qol-bundle.svg" alt="Included in Serilum&#x27;s QoL Bundle" width="334" height="49"></a><br><img src="https://cdn.serilum.com/description/badges/environment/both.svg" alt="Environment: Client &amp; Server" width="293" height="49"></p>
<h2><br><br><br><img src="https://cdn.serilum.com/description/nameplate/configurable.svg" alt="Configurable" width="838" height="64"></h2>
<p><a href="https://serilum.com/configuration-guide" target="_blank" rel="nofollow"><img src="https://cdn.serilum.com/description/badges/configuration-guide.svg" alt="Configuration guide" width="220" height="49"></a></p>
<p>&nbsp;&nbsp;&nbsp;<span style="font-size:18px">All available config options:</span></p>
<div class="spoiler" style="margin-left:10px;padding-left:10px">
<p><span style="font-size:14px"><strong>refillBlocksAndItems</strong></span><span style="font-size:12px;color:#8a8d91">&nbsp;&nbsp;default true</span><br><span style="font-size:13px">Whether the hand is refilled when the last item of a stack is placed, used, eaten or thrown. For example blocks, food, bone meal, eggs and potions.</span><br><br><span style="font-size:14px"><strong>refillToolsAndWeapons</strong></span><span style="font-size:12px;color:#8a8d91">&nbsp;&nbsp;default true</span><br><span style="font-size:13px">Whether an item with durability is replaced by the same item from the inventory when it breaks or is thrown. For example tools, swords, fishing rods and tridents.</span><br><br><span style="font-size:14px"><strong>refillAfterDroppingLastItem</strong></span><span style="font-size:12px;color:#8a8d91">&nbsp;&nbsp;default true</span><br><span style="font-size:13px">Whether the main hand is refilled when its last item is dropped with the drop key.</span><br><br><span style="font-size:14px"><strong>refillBuckets</strong></span><span style="font-size:12px;color:#8a8d91">&nbsp;&nbsp;default true</span><br><span style="font-size:13px">Whether buckets are refilled. When disabled, a bucket that is emptied or filled stays in the hand instead of being swapped for another bucket from the inventory.</span><br><br><span style="font-size:14px"><strong>refillFromOtherHotbarSlots</strong></span><span style="font-size:12px;color:#8a8d91">&nbsp;&nbsp;default false</span><br><span style="font-size:13px">Whether the other hotbar slots are searched as well. When disabled, only the main inventory is used to refill the hand.</span><br><br><span style="font-size:14px"><strong>itemBlacklist</strong></span><span style="font-size:12px;color:#8a8d91">&nbsp;&nbsp;default ""</span><br><span style="font-size:13px">A comma separated list of item ids that are never refilled. Use * as a wildcard. An id without a namespace matches items of every mod. For example 'minecraft:totem_of_undying,*shulker_box'.</span><br><br><span style="font-size:14px"><strong>itemWhitelist</strong></span><span style="font-size:12px;color:#8a8d91">&nbsp;&nbsp;default ""</span><br><span style="font-size:13px">When not empty, only the items in this comma separated list are refilled. Same format as itemBlacklist, for example 'minecraft:*_planks,cobblestone'.</span><br><br></p>
</div>
<h2><br><br><br><img src="https://cdn.serilum.com/description/nameplate/commands.svg" alt="Commands" width="838" height="64"></h2>
<p><span style="font-size:18px"><em>/stackrefill toggle</em> - Turns Stack Refill on or off for yourself.</span></p>
<p><span style="font-size:18px"><em>/stackrefill enable</em> - Turns Stack Refill on for yourself.</span></p>
<p><span style="font-size:18px"><em>/stackrefill disable</em> - Turns Stack Refill off for yourself.</span></p>
<p><span style="font-size:18px"><em>/stackrefill help</em> - Shows the usage of commands.</span></p>
<h2><br><br><br><img src="https://cdn.serilum.com/description/nameplate/showcase.svg" alt="Showcase" width="838" height="64"></h2>
<p><span style="font-size:14px"><strong>A sword being replaced during combat:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><picture><img src="https://cdn.serilum.com/projects/stack-refill/a.webp" alt="" width="1000" height="584"></picture></p>
</details>
<p><br><span style="font-size:14px"><strong>A shovel being replaced when digging dirt:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><picture><img src="https://cdn.serilum.com/projects/stack-refill/b.webp" alt="" width="1000" height="584"></picture></p>
</details>
<p><br><span style="font-size:14px"><strong>Log blocks being replaced while placing them down:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><picture><img src="https://cdn.serilum.com/projects/stack-refill/c.webp" alt="" width="1000" height="586"></picture></p>
</details>
<p><br><span style="font-size:14px"><strong>Bread being replaced after consumption:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><picture><img src="https://cdn.serilum.com/projects/stack-refill/d.webp" alt="" width="1000" height="588"></picture></p>
</details>
<p><br><span style="font-size:14px"><strong>Potions being replaced after consumption:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><picture><img src="https://cdn.serilum.com/projects/stack-refill/e.webp" alt="" width="1000" height="588"></picture></p>
</details>
<p><br><span style="font-size:14px"><strong>Items replaced after being tossed:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><picture><img src="https://cdn.serilum.com/projects/stack-refill/f.webp" alt="" width="1000" height="608"></picture></p>
</details>
<p><br><span style="font-size:14px"><strong>A milk bucket being replaced after being consumed:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><picture><img src="https://cdn.serilum.com/projects/stack-refill/g.webp" alt="" width="1000" height="580"></picture></p>
</details>
<p>&nbsp;<br><span style="font-size:14px"><strong>A fresh fishing rod after its last use:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><picture><img src="https://cdn.serilum.com/projects/stack-refill/h.webp" alt="" width="1000" height="580"></picture></p>
</details>
<p><br><span style="font-size:14px"><strong>When using items on (modded) blocks, such as a sieve from Ex Nihilo:</strong></span></p>
<details><summary>Show Spoiler</summary>
<p><span style="font-size:14px"><picture><img src="https://cdn.serilum.com/projects/stack-refill/i.webp" alt="" width="1000" height="516"></picture></span></p>
</details>
<h2><br><br><br><img src="https://cdn.serilum.com/description/nameplate/notes-n-links.svg" alt="Notes and links" width="838" height="64"></h2>
<p><span style="font-size:24px"><strong>You may freely use this mod in any modpack, as long as the download remains hosted within the CurseForge or Modrinth ecosystem.</strong></span><br><br><span style="font-size:18px"><a style="font-size:18px;color:#008000" href="https://serilum.com/" target="_blank" rel="nofollow">Serilum.com</a> contains an overview and more information on all mods available.</span><br><br><span style="font-size:14px">Comments are disabled as I'm unable to keep track of them across so many separate mod pages.</span><span style="font-size:14px"><br>For issues, ideas, suggestions or anything else there is the&nbsp;<a style="font-size:14px;color:#008000" href="https://github.com/Serilum/.issue-tracker" target="_blank" rel="nofollow">Github repo</a>. Thanks!</span><span style="font-size:6px"><br><br></span></p>