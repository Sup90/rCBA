Vagrant.configure("2") do |config|
	config.vm.box = "ubuntu/trusty64"
	config.vm.network :forwarded_port, guest: 8787, host: 8787
	config.vm.provider "virtualbox" do |v|
		v.memory = 8192
		v.cpus = 4
	end
	config.vm.synced_folder ".", "/home/vagrant/rCBA"
	config.vm.provision "shell", path: "provision.sh"
end