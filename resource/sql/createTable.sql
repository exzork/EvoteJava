CREATE TABLE IF NOT EXISTS `calon` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `npmKetua` varchar(20) NOT NULL,
  `npmWakil` varchar(20) DEFAULT NULL,
  `foto` mediumblob DEFAULT NULL,
  `visi` text DEFAULT NULL,
  `misi` text DEFAULT NULL,
  `suara` int(11) DEFAULT 0,
  `idPemilihan` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `daftarpemilih` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `npm` varchar(11) NOT NULL,
  `idEvent` int(11) NOT NULL,
  `used` tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE (`npm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `event` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` text NOT NULL,
  `foto` mediumblob DEFAULT NULL,
  `deskripsi` text NOT NULL,
  `enable` tinyint(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `panitia` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `npm` varchar(20) NOT NULL,
  `jabatan` text NOT NULL,
  `idEvent` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `pemilihan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` text NOT NULL,
  `idEvent` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `user` (
  `npm` varchar(20) NOT NULL,
  `nama` text NOT NULL,
  `email` text NOT NULL,
  `password` text NOT NULL,
  PRIMARY KEY (`npm`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
