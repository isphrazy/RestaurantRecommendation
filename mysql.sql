#
# Table structure for table 'members'
#
CREATE TABLE `members` (
  `id` int(4) NOT NULL auto_increment,
  `access_token` varchar(65) NOT NULL default '',
  `username` varchar(65) NOT NULL default '',
  `password` varchar(65) NOT NULL default '',
  `email` varchar(65) NOT NULL default '',
  PRIMARY KEY  (`id`)
) TYPE=MyISAM;

#
# Table structure for table 'likes'
#
CREATE TABLE `likes` (
  `uid` int(4) NOT NULL,
  `rid` varchar(65) NOT NULL default ''
) TYPE=MyISAM;