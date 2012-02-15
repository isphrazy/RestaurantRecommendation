<?php
include 'background_profile.php';
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
			<a href="detail.php?name=<?=$rid?>">
				<b><?=$restaurant["Business Name"] . ', '?></b>
				<?=$restaurant["Address"]?>
			</a>
			<a href="javascript:void(0)" onclick="removeLike('<?=$rid?>');">
				<img src="b_drop.png" alt="b_drop.png" class="like" />
			</a>
		</td>
	</tr>
	<?php
}?>
</table>

<?php
print_bottom();
?>