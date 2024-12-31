CREATE TABLE `posting_data` (
  `posting_id` int(11) NOT NULL AUTO_INCREMENT,
  `posting_type` enum('REGULAR','INTERNSHIP') NOT NULL DEFAULT 'REGULAR',
  `creator_id` varchar(64) DEFAULT NULL,
  `job_title` varchar(64) NOT NULL DEFAULT ' ',
  `job_state` enum('AK','AL','AR','AZ','CA','CO','CT','DC','DE','FL','GA','HI','IA','ID','IL','IN','KS','KY','LA','MA','MD','ME','MI','MN','MO','MS','MT','NC','ND','NE','NH','NJ','NM','NV','NY','OH','OK','OR','PA','RI','SC','SD','TN','TX','UT','VA','VT','WA','WI','WV','WY','NA') DEFAULT 'NA',
  `company_name` varchar(64) NOT NULL DEFAULT ' ',
  `contact_email` varchar(64) NOT NULL DEFAULT ' ',
  `major` varchar(64) DEFAULT ' ',
  `classification` varchar(64) DEFAULT ' ',
  `req_experience` int(11) DEFAULT '0',
  `salary` int(11) DEFAULT '0',
  PRIMARY KEY (`posting_id`),
  UNIQUE KEY `posting_id_UNIQUE` (`posting_id`)
) ENGINE=InnoDB AUTO_INCREMENT=100009 DEFAULT CHARSET=latin1
