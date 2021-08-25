
pipeline {
    agent any
    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello World"'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
            }
        }
        
        stage('Checkout cyclop yaml') {
            steps {
                dir('cyclop_yaml') {
                    git branch: 'cyclop-linux',
                        url: 'ssh://git@bitbucket.it.keysight.com:7999/cyclop/cyclop_yaml.git'
                        
                    sh "ls -lat"    
                }
            }
        }
        
        stage('Checkout bitbake script') {
            steps {
                dir('bitbake_script') {
                    git branch: 'develop',
                        url: 'ssh://git@bitbucket.it.keysight.com:7999/myve/bitbake-scripts.git'
                        
                    sh "ls -lat"    
                }
            }
        }
        
        stage('Prepare bitbake') {
            steps {
                dir('bitbake_script') {
                    sh "./buildtask_prepare_bitbake_workspace.py --bitbake_manifest ../cyclop_yaml/p700-cyclop.yml --bitbake_workspace ~/hpp-dev"    
                }
            }     
        }
        
        stage('Starting bitbake') {
            steps {
                dir('bitbake_script') {
                    sh "./buildtask_bitbake.py --bitbake_manifest ../cyclop_yaml/p700-cyclop.yml --bitbake_workspace ~/hpp-dev --bitbake_target instrument"    
                }
            }     
        }        
    }
}
