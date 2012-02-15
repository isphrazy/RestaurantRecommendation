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
foreach ($restaurants as $restaurant) {
  ?>
	<tr><td>
		<a href="">
			<b><?=$restaurant["Business Name"] . ', '?></b>
			<?=$restaurant["Address"]?>
		</a>
	</td></tr>
	<?php
}?>
</table>

<?php
print_bottom();
?>