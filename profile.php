<?php
include 'backend/b_profile.php';
include 'pattern.php';

print_head();
print_login();
print_search_bar();
?>

<h1 id="profile">My Profile:</h1>
<table>
<tr><td class='didyou'>You liked the following restaurants:</td></tr>
<?php
get_restaurants();
foreach ($restaurants as $rid => $restaurant) {
	?>
	<tr id="<?=$rid?>">
		<td>
			<a href="detail.php?name=<?=$rid?>" target="_blank">
				<b><?=$restaurant["Business Name"] . ', '?></b>
				<?=$restaurant["Address"]?>
			</a>
			<a href="javascript:void(0)" onclick="unlike('<?=$rid?>');">
				<img src="static/b_drop.png" alt="static/b_drop.png" class="like" />
			</a>
		</td>
	</tr>
	<?php
}?>
</table>

<?php
print_bottom();
?>